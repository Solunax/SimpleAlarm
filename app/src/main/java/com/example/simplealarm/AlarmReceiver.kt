package com.example.simplealarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.simplealarm.room.AlarmDAO
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var alarmDAO: AlarmDAO

    companion object{
        const val channelID = "SimpleAlarm"
        const val notificationID = 10
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context : Context, intent : Intent?) {
        createNotificationChannel(context)
        startNotify(context)

        // 알람 발생 시 내부 DB에 저장된 알람 활성 상태를 비활성화 시킴
        Thread{
            alarmDAO.editAlarmState(intent!!.getIntExtra("id", 0), false)
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context){
        val notificationChannel = NotificationChannel(
            channelID,
            "알람",
            NotificationManager.IMPORTANCE_HIGH
        )

        NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
    }

    private fun startNotify(context: Context){
        // Scope 함수 - with : run 같은 . 을 사용하는 참조연산자 형태가 아닌 파라미터 형태로 사용
        with(NotificationManagerCompat.from(context)){
            val intent = Intent(context, MainActivity::class.java)

            val pendingIntent = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            } else {
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            val build = NotificationCompat.Builder(context, channelID)
                .setContentTitle("알람")
                .setContentText("알람 시간입니다.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)

            try{
                notify(notificationID, build.build())
            }catch (e : SecurityException){
                Toast.makeText(context, "권한을 설정하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}