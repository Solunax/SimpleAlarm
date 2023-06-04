package com.example.simplealarm.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.simplealarm.databinding.TimeSetDialogBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class TimeSetDialog(context : Context, mInterface : TimePickerInterface) : Dialog(context) {
    private var customInterface : TimePickerInterface = mInterface
    private lateinit var binding : TimeSetDialogBinding
    var hour : Int? = null
    var minute : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = TimeSetDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val timePicker = binding.timePicker
        val ok = binding.timeSet
        val no = binding.cancel

        timePicker.setOnTimeChangedListener { _, h, m ->
            hour = h
            minute = m
        }

        ok.setOnClickListener {
            if(hour == null && minute == null)
                getTime()

            customInterface.onPositive(hour!!, minute!!)
            dismiss()
        }

        no.setOnClickListener {
            dismiss()
        }
    }

    // 사용자가 시간을 선택하지 않을시 현재 시간을 기준으로 알람을 설정하기 위한 메소드
    // 현재 시간을 받아와 시, 분을 설정
    private fun getTime(){
        val format = SimpleDateFormat("HH:mm")
        val now = System.currentTimeMillis()
        val time = format.format(now).split(":")

        hour = time[0].toInt()
        minute = time[1].toInt()
    }
}