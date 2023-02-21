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
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.simplealarm.databinding.ActivityMainBinding
import com.example.simplealarm.dialog.TimePicekrInterface
import com.example.simplealarm.dialog.TimeSetDialog
import com.example.simplealarm.room.Alarm
import java.util.*

class MainActivity : AppCompatActivity(), TimePicekrInterface {
    private val viewModel : ViewModel by viewModels()
    private lateinit var binding : ActivityMainBinding
    private lateinit var alarm : Alarm
    private lateinit var calendar : Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var controlAlarm : Button
    private val alarmREQCode = 100

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendar = Calendar.getInstance()
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val time = binding.time
        controlAlarm = binding.setAlarm
        val setTime = binding.timeSet

        viewModel.alarmData.observe(this){
            if(it.isNotEmpty()){
                alarm = it.first()
                time.text = "%02d:%02d".format(alarm.hour, alarm.minute)

                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    alarmREQCode,
                    Intent(this, AlarmReceiver::class.java),
                    PendingIntent.FLAG_NO_CREATE
                )

                if((pendingIntent == null) && alarm.isOn){
                    alarm.isOn = false
                    viewModel.addAlarm(alarm)
                }else if((pendingIntent != null) && !alarm.isOn)
                    pendingIntent.cancel()

                if(alarm.isOn){
                    Log.d("ON", "ON")
                    controlAlarm.text = "알람 끄기"
                }
                else{
                    Log.d("OFF", "OFF")
                    controlAlarm.text = "알람 켜기"
                }
            }
        }

        setTime.setOnClickListener {
            val timePickerDialog = TimeSetDialog(this, this)
            timePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            timePickerDialog.show()
        }

        controlAlarm.setOnClickListener {
            if(::alarm.isInitialized){
                if(!alarm.isOn){
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
                    intent.putExtra("id", 0)

                    val pendingIntent = PendingIntent.getBroadcast(
                        this,
                        alarmREQCode,
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
                    controlAlarm.text = "알람 끄기"
                }else{
                    cancelAlarm()

                    alarm.isOn = false
                    viewModel.addAlarm(alarm)

                    Log.d("SET", "알람 중지됨")
                    controlAlarm.text = "알람 켜기"
                }
            }
        }
    }

    private fun cancelAlarm(){
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            alarmREQCode,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        pendingIntent?.cancel()
    }

    override fun onPositive(hour: Int, minute: Int) {
        val alarm = Alarm(0, hour, minute, false)
        viewModel.addAlarm(alarm)
    }
}