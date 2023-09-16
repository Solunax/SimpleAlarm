package com.example.simplealarm

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.simplealarm.repository.LocalRepository
import com.example.simplealarm.repository.RepositoryInterface
import com.example.simplealarm.room.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// @Qualifier 로 Repository 를 구분, 어노테이션을 통해 어떤 Repository 인지 구분해 주어야 함
@HiltViewModel
class AlarmViewModel @Inject constructor(@LocalRepository private val repository: RepositoryInterface) : androidx.lifecycle.ViewModel() {
    private val _alarmData : LiveData<List<Alarm>> = repository.alarmData.asLiveData()
    val alarmData get() = _alarmData

    fun addAlarm(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlarm(alarm)
        }
    }

    fun deleteAlarm(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(id)
        }
    }

    fun editAlarmState(id : Int, state : Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            repository.editAlarmState(id, state)
        }
    }

    fun validateCheck(context : Context, activity: Activity){
        _alarmData.value?.forEach { v ->
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                v.alarmID,
                Intent(activity, AlarmReceiver::class.java),
                PendingIntent.FLAG_NO_CREATE
            )

            // pending intent 가 존재하지 않는데, 알람이 활성화 되어있으면 내부 DB 알람 상태를 변경
            // pending intent 가 존재하지만 알람이 비활성화 되어있으면 pending intent를 취소함
            if((pendingIntent == null) && v.isOn){
                this.editAlarmState(v.alarmID, false)
            }else if((pendingIntent != null) && !v.isOn)
                pendingIntent.cancel()
        }
    }
}