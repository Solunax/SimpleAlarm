package com.example.simplealarm.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplealarm.dialog.TimePickerInterface
import com.example.simplealarm.dialog.TimeSetDialog
import com.example.simplealarm.room.Alarm
import androidx.fragment.app.viewModels
import com.example.simplealarm.AlarmReceiver
import com.example.simplealarm.alarmRecycler.AlarmRecyclerAdapter
import com.example.simplealarm.AlarmViewModel
import com.example.simplealarm.alarmRecycler.AlarmRecyclerClickCallback
import com.example.simplealarm.databinding.AlarmFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AlarmFragment : Fragment(), TimePickerInterface, AlarmRecyclerClickCallback {
    private var binding : AlarmFragmentBinding? = null
    private val viewModel : AlarmViewModel by viewModels()
    private lateinit var alarm : Alarm
    private lateinit var calendar : Calendar
    private lateinit var alarmManager: AlarmManager
    private val recyclerAdapter = AlarmRecyclerAdapter(this)
    private var alarmData : List<Alarm> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AlarmFragmentBinding.inflate(inflater, container, false)
        calendar = Calendar.getInstance()
        alarmManager = getSystemService(requireActivity(), AlarmManager::class.java) as AlarmManager

        val setTime = binding!!.timeSet
        val recyclerView = binding!!.alarmRecycler
        recyclerAdapter.setHasStableIds(true)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter

        viewModel.alarmData.observe(viewLifecycleOwner){
            alarmData = it
            recyclerAdapter.setData(alarmData)
            viewModel.validateCheck(requireContext(), requireActivity())
        }

        setTime.setOnClickListener {
            val timePickerDialog = TimeSetDialog(requireContext(), this)
            timePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            timePickerDialog.show()
        }

        return binding!!.root
    }

    // 알람 취소 메소드
    private fun cancelAlarm(position: Int){
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            position,
            Intent(activity, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        pendingIntent?.cancel()
    }

    // Custom Dialog 의 Interface 구현 메소드
    override fun onPositive(hour: Int, minute: Int) {
        val alarm = Alarm(0, hour, minute, false)
        viewModel.addAlarm(alarm)
    }

    // RecyclerView 에서 발생한 이벤트 처리를 위한 Interface 구현 메소드
    @RequiresApi(Build.VERSION_CODES.M)
    // Recycler View 에서 클릭 이벤트 발생시(CheckBox)
    override fun onClick(position: Int, state: Boolean){
        if(::alarm.isInitialized){
            // 알람 데이터 중 현재 선택된 알람의 인스턴스를 가져옴
            alarmData.forEach {
                if(it.alarmID == position)
                    alarm = it
            }

            // Check Box 의 상태에 따라 다른 동작 실행
            // Check Box 가 활성 -> 새로운 알람을 Alarm Manager 에 설정
            // Check Box 가 비활성 -> 등록된 알람을 Alarm Manager 에서 제거
            if(state){
                val now = Calendar.getInstance().time

                calendar.apply {
                    set(Calendar.HOUR_OF_DAY, alarm.hour)
                    set(Calendar.MINUTE, alarm.minute)
                    set(Calendar.SECOND, 0)
                }

                if(now.after(calendar.time)){
                    Toast.makeText(context, "다음 날로 알람을 설정 합니다.", Toast.LENGTH_SHORT).show()
                    calendar.add(Calendar.DATE, 1)
                }

                val intent = Intent(activity, AlarmReceiver::class.java)
                intent.putExtra("id", position)

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    position,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )

                alarm.isOn = true
                Log.d("SET", "알람 설정됨")
            }else{
                cancelAlarm(position)

                alarm.isOn = false
                Log.d("SET", "알람 중지됨")
            }

            viewModel.editAlarmState(alarm.alarmID, alarm.isOn)
        }
    }

    // RecyclerView 에서 발생한 이벤트 처리를 위한 Interface 구현 메소드
    override fun onClickDelete(position: Int) {
        cancelAlarm(position)
        viewModel.deleteAlarm(position)
    }
}