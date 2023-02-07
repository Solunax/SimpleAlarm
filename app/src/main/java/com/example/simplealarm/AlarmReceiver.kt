package com.example.simplealarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {

    companion object{
        const val channelID = "SimpleAlarm"
        const val notificationID = 10
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context : Context, intent : Intent?) {
        Log.d("WORK", "WORKING")
        createNotificationChannel(context)
        startNotify(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context){
        val notificationChannel = NotificationChannel(
            channelID,
            "알림",
            NotificationManager.IMPORTANCE_HIGH
        )

        NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
    }

    private fun startNotify(context: Context){
        with(NotificationManagerCompat.from(context)){
            val build = NotificationCompat.Builder(context, channelID)
                .setContentTitle("알람")
                .setContentText("알람 시간입니다.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
            try{
                notify(notificationID, build.build())
            }catch (e : SecurityException){
                Toast.makeText(context, "권한을 설정하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}