package com.example.simplealarm.repository

import com.example.simplealarm.room.Alarm
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    var alarmData : Flow<List<Alarm>>
    fun addAlarm(alarm: Alarm)
    fun deleteAlarm(id: Int)
}