package com.example.simplealarm.lapTimeRecycler

data class StopWatchTimeData (var hour : Int, var minute : Int, var second : Int, var milSec : Int){
    // 객체의 값을 초기화하는 메소드
    fun reset(){
        hour = 0
        minute = 0
        second = 0
        milSec = 0
    }


    // 객체의 시간을 계산하는 메소드
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