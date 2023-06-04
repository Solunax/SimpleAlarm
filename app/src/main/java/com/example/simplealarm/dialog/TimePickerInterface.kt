package com.example.simplealarm.dialog

// Custom Dialog 에서 시간 선택 시 해당 시간 정보를 Activity로 넘겨주기 위한 인터페이스
interface TimePickerInterface {
    fun onPositive(hour : Int, minute : Int)
}