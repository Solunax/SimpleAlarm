package com.example.simplealarm

import com.example.simplealarm.room.Alarm
import com.example.simplealarm.room.AppDataBase
import kotlinx.coroutines.flow.Flow

class Repository(private val db : AppDataBase?) {
    val alarmData : Flow<List<Alarm>> = db!!.AlarmDAO().getAlarmList()

    fun addAlarm(alarm: Alarm){
        db!!.AlarmDAO().addAlarm(alarm)
    }

    fun deleteAlarm(alarm: Alarm){
        db!!.AlarmDAO().deleteAlarm(alarm)
    }
}