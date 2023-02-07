package com.example.simplealarm.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun AlarmDAO() : AlarmDAO

    companion object{
        private var instance : AppDataBase? = null

        fun getInstance(context : Context) : AppDataBase? {
            if(instance == null){
                synchronized(AppDataBase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "AlarmDB"
                    ).build()
                }
            }
            return instance
        }
    }
}