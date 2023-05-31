package com.example.simplealarm.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.simplealarm.databinding.TimeSetDialogBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class TimeSetDialog(context : Context, mInterface : TimePicekrInterface) : Dialog(context) {
    private var customInterface : TimePicekrInterface = mInterface
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
        getTime()

        timePicker.setOnTimeChangedListener { _, h, m ->
            hour = h
            minute = m
        }

        ok.setOnClickListener {
            if(hour != null && minute != null){
                customInterface.onPositive(hour!!, minute!!)
                dismiss()
            }else
                Toast.makeText(context, "시간을 설정하세요.", Toast.LENGTH_SHORT).show()
        }

        no.setOnClickListener {
            dismiss()
        }
    }

    fun getTime(){
        val format = SimpleDateFormat("HH:mm")
        val now = System.currentTimeMillis()
        val time = format.format(now).split(":")

        hour = time[0].toInt()
        minute = time[1].toInt()
    }
}