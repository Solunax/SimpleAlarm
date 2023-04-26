package com.example.simplealarm

import com.example.simplealarm.room.Alarm
import com.example.simplealarm.room.AlarmDAO
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class Repository @Inject constructor(private val db : AlarmDAO) {
    val alarmData : Flow<List<Alarm>> = db.getAlarmList()

    fun addAlarm(alarm: Alarm){
        db.addAlarm(alarm)
    }

    fun deleteAlarm(id : Int){
        db.deleteAlarm(id)
    }
}