package com.example.simplealarm.repository

import com.example.simplealarm.room.Alarm
import com.example.simplealarm.room.AlarmDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepository @Inject constructor(private val db: AlarmDAO) : RepositoryInterface {
    override var alarmData: Flow<List<Alarm>> = db.getAlarmList()

    override fun addAlarm(alarm: Alarm) {
        db.addAlarm(alarm)
    }

    override fun deleteAlarm(id: Int) {
        db.deleteAlarm(id)
    }
}