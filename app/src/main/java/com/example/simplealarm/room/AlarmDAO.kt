package com.example.simplealarm.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDAO {
    @Insert(onConflict = REPLACE)
    fun addAlarm(alarm : Alarm)

    @Query("DELETE FROM Alarm WHERE alarmID = :id")
    fun deleteAlarm(id : Int)

    @Query("SELECT * FROM Alarm ORDER BY hour, minute")
    fun getAlarmList() : Flow<List<Alarm>>
}