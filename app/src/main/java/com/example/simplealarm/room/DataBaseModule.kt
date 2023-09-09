package com.example.simplealarm.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// 외부 라이브러리를  의존성 주입하기 위해 생성한 object
// 외부 라이브러리는 @Provides 어노테이션을 사용하여 인스턴스 제공방법을 정의, @Binds 는 사용 불가
@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context : Context) : AppDataBase{
        return Room.databaseBuilder(context, AppDataBase::class.java, "AlarmDB").build()
    }

    @Provides
    fun provideAlarmDAO(appDataBase: AppDataBase): AlarmDAO {
        return appDataBase.AlarmDAO()
    }
}