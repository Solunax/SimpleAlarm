package com.example.simplealarm.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDAO {
    @Insert(onConflict = REPLACE)
    fun addAlarm(alarm : Alarm)

    @Delete
    fun deleteAlarm(alarm : Alarm)

    @Query("SELECT * FROM Alarm")
    fun getAlarmList() : Flow<List<Alarm>>
}