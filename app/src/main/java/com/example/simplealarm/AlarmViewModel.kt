package com.example.simplealarm

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.simplealarm.repository.LocalRepository
import com.example.simplealarm.repository.RepositoryInterface
import com.example.simplealarm.room.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

// @Qualifier 로 Repository 를 구분, 어노테이션을 통해 어떤 Repository 인지 구분해 주어야 함
@HiltViewModel
class AlarmViewModel @Inject constructor(application : Application, @LocalRepository private val repository : RepositoryInterface) : AndroidViewModel(application) {
    private val _alarmData : LiveData<List<Alarm>> = repository.alarmData.asLiveData()
    val alarmData get() = _alarmData
    private var _event = MutableLiveData<Event<String>>()
    val event get() = _event
    val calendar: Calendar = Calendar.getInstance()
    val alarmManager = ContextCompat.getSystemService(getApplication(), AlarmManager::class.java) as AlarmManager
    private lateinit var alarm : Alarm

    fun addAlarm(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlarm(alarm)
        }
    }

    fun deleteAlarm(id: Int){
        cancelAlarm(id)
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(id)
        }
    }

    private fun editAlarmState(id : Int, state : Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            repository.editAlarmState(id, state)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setAlarm(position: Int, state: Boolean){
        // 알람 데이터 중 현재 선택된 알람의 인스턴스를 가져옴
        alarmData.value?.forEach {
            if(it.alarmID == position)
                alarm = it
        }

        // View의 Check Box 의 상태에 따라 다른 동작 실행
        // Check Box가 활성 -> 새로운 알람을 Alarm Manager 에 설정
        // Check Box가 비활성 -> 등록된 알람을 Alarm Manager 에서 제거
        if(state){
            var tomorrowCheck = false
            val now = Calendar.getInstance().time

            calendar.apply {
                set(Calendar.HOUR_OF_DAY, alarm.hour)
                set(Calendar.MINUTE, alarm.minute)
                set(Calendar.SECOND, 0)
            }

            if(now.after(calendar.time)){
                tomorrowCheck = true
                calendar.add(Calendar.DATE, 1)
            }

            // 알람에 추가할 Intent 인스턴스
            val intent = Intent(getApplication(), AlarmReceiver::class.java)
            intent.putExtra("id", position)

            // Android 버전에 따라 Flag를 다르게 설정
            val pendingIntent = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                PendingIntent.getBroadcast(
                    getApplication(),
                    position,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }else{
                PendingIntent.getBroadcast(
                    getApplication(),
                    position,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            // 정확한 시간을 보장하고 Doze 모드에서도 알람이 울림
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            alarm.isOn = true
            // 알람 설정이 성공적으로 완료됐다는 Event 발생
            _event.value = Event("Alarm Set Success")

            // 알람이 다음날로 설정 됐을 경우 Calendar 인스턴스에 더한 하루를 빼줌
            if(tomorrowCheck)
                calendar.add(Calendar.DATE, -1)
        }else{
            cancelAlarm(position)
            alarm.isOn = false
        }

        editAlarmState(alarm.alarmID, alarm.isOn)
    }

    // 알람 취소 메소드
    private fun cancelAlarm(position: Int){
        val pendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            position,
            Intent(getApplication(), AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        pendingIntent?.cancel()
    }

    // 알람 유효성 검사
    fun alarmValidateCheck(context : Context, activity: Activity){
        _alarmData.value?.forEach { v ->
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                v.alarmID,
                Intent(activity, AlarmReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )

            // pending intent 가 존재하지 않는데, 알람이 활성화 되어있으면 내부 DB 알람 상태를 변경
            // pending intent 가 존재하지만 알람이 비활성화 되어있으면 pending intent를 취소함
            if((pendingIntent == null) && v.isOn){
                this.editAlarmState(v.alarmID, false)
            }else if((pendingIntent != null) && !v.isOn){
                pendingIntent.cancel()
            }
        }
    }
}