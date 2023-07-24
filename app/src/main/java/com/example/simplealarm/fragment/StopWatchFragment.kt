package com.example.simplealarm.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.simplealarm.databinding.ChronoMeterFragmentBinding

class StopWatchFragment : Fragment() {
    private var binding : ChronoMeterFragmentBinding? = null
    var time = 0L
    var state = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChronoMeterFragmentBinding.inflate(inflater, container, false)

        val start = binding!!.start
        val reset = binding!!.reset
        val chronometer = binding!!.chronometer

        start.setOnClickListener {
            when(state){
                true -> {
                    time = chronometer.base - SystemClock.elapsedRealtime()
                    chronometer.stop()
                    start.text = "시작"
                    reset.isEnabled = true
                }
                false -> {
                    chronometer.base = SystemClock.elapsedRealtime() + time
                    chronometer.start()
                    start.text = "정지"
                    reset.isEnabled = false
                }
            }

            state = !state
        }

        reset.setOnClickListener {
            time = 0L
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.stop()

            reset.isEnabled = false
        }

        return binding!!.root
    }
}