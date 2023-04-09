package com.example.simplealarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplealarm.databinding.ActivityMainBinding
import com.example.simplealarm.dialog.TimePicekrInterface
import com.example.simplealarm.dialog.TimeSetDialog
import com.example.simplealarm.room.Alarm
import java.util.*

class MainActivity : AppCompatActivity(), TimePicekrInterface, RecyclerClickCallback {
    private val viewModel : ViewModel by viewModels()
    private lateinit var binding : ActivityMainBinding
    private lateinit var alarm : Alarm
    private lateinit var calendar : Calendar
    private lateinit var alarmManager: AlarmManager
    private val recyclerAdapter = AlarmRecyclerAdapter(this)
    private var alarmData : List<Alarm> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendar = Calendar.getInstance()
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val setTime = binding.timeSet
        val recyclerView = binding.alarmRecycler
        recyclerAdapter.setHasStableIds(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter

        viewModel.alarmData.observe(this){
            alarmData = it
            recyclerAdapter.setData(it)
            if(it.isNotEmpty()){
                it.forEach {v ->
                    alarm = v

                    val pendingIntent = PendingIntent.getBroadcast(
                        this,
                        alarm.alarmID,
                        Intent(this, AlarmReceiver::class.java),
                        PendingIntent.FLAG_NO_CREATE
                    )

                    if((pendingIntent == null) && alarm.isOn){
                        alarm.isOn = false
                        viewModel.addAlarm(alarm)
                    }else if((pendingIntent != null) && !alarm.isOn)
                        pendingIntent.cancel()

                    if(alarm.isOn)
                        Log.d("ON", "ON")
                    else
                        Log.d("OFF", "OFF")
                }

            }
        }

        setTime.setOnClickListener {
            val timePickerDialog = TimeSetDialog(this, this)
            timePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            timePickerDialog.show()
        }
    }

    private fun cancelAlarm(position: Int){
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            position,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        pendingIntent?.cancel()
    }

    override fun onPositive(hour: Int, minute: Int) {
        val alarm = Alarm(0, hour, minute, false)
        viewModel.addAlarm(alarm)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(position: Int, state: Boolean){
        if(::alarm.isInitialized){
            alarmData.forEach {
                if(it.alarmID == position)
                    alarm = it
            }

            if(state){
                val now = Calendar.getInstance().time

                calendar.apply {
                    set(Calendar.HOUR_OF_DAY, alarm.hour)
                    set(Calendar.MINUTE, alarm.minute)
                    set(Calendar.SECOND, 0)
                }

                if(now.after(calendar.time)){
                    Toast.makeText(this, "다음 날로 알람을 설정 합니다.", Toast.LENGTH_SHORT).show()
                    calendar.add(Calendar.DATE, 1)
                }

                val intent = Intent(this, AlarmReceiver::class.java)
                intent.putExtra("id", position)

                val pendingIntent = PendingIntent.getBroadcast(
                    this,
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
                viewModel.addAlarm(alarm)

                Log.d("SET", "알람 설정됨")
            }else{
                cancelAlarm(position)

                alarm.isOn = false
                viewModel.addAlarm(alarm)

                Log.d("SET", "알람 중지됨")
            }
        }
    }

    override fun onClickDelete(position: Int) {
        cancelAlarm(position)
        viewModel.deleteAlarm(position)
    }
}