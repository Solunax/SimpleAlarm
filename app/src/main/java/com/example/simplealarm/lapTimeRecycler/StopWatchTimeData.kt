package com.example.simplealarm.lapTimeRecycler

data class StopWatchTimeData (var hour : Int, var minute : Int, var second : Int, var milSec : Int){
    fun reset(){
        hour = 0
        minute = 0
        second = 0
        milSec = 0
    }

    fun calculateTime(){
        if(milSec == 100){
            second++
            milSec = 0
        }

        if(second == 60){
            minute++
            second = 0
        }

        if(minute == 60){
            hour++
            minute = 0
        }
    }
}