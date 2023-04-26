package com.example.simplealarm.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun AlarmDAO() : AlarmDAO
}