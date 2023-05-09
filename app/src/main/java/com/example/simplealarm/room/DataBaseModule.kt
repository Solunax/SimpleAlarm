package com.example.simplealarm.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context : Context) : AppDataBase{
        return Room.databaseBuilder(context, AppDataBase::class.java, "AlarmDB").build()
    }

    @Provides
    fun provideAlarmDAI(appDataBase: AppDataBase): AlarmDAO {
        return appDataBase.AlarmDAO()
    }
}