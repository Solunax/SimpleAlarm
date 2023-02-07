package com.example.simplealarm.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.simplealarm.databinding.TimeSetDialogBinding

class TimeSetDialog(context : Context, mInterface : TimePicekrInterface) : Dialog(context) {
    private var customInterface : TimePicekrInterface = mInterface
    private lateinit var binding : TimeSetDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = TimeSetDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val timePicker = binding.timePicker
        val ok = binding.timeSet
        val no = binding.cancel

        var hour : Int? = null
        var minute : Int? = null

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
}