package com.example.simplealarm.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alarm")
data class Alarm (
    @PrimaryKey
    val alarmID : Int = 0,
    val hour : Int,
    val minute : Int,
    var isOn : Boolean)