package com.example.simplealarm.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplealarm.databinding.ChronoMeterFragmentBinding
import com.example.simplealarm.lapTimeRecycler.LapData
import com.example.simplealarm.lapTimeRecycler.LapTimeRecyclerAdapter

class StopWatchFragment : Fragment() {
    private var binding : ChronoMeterFragmentBinding? = null
    private val recyclerAdapter = LapTimeRecyclerAdapter()
    private var time = 0L
    private var startState = false
    private var lapState = false
    private var lapTimeArrayList = ArrayList<LapData>()
    private var lapTimeIndex = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChronoMeterFragmentBinding.inflate(inflater, container, false)

        val startStop = binding!!.startStop
        val lapReset = binding!!.lapReset
        val chronometer = binding!!.chronometer
        val recyclerView = binding!!.lapTimeRecycler

        recyclerAdapter.setHasStableIds(true)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter

        startStop.setOnClickListener {
            when(startState){
                true -> {
                    time = chronometer.base - SystemClock.elapsedRealtime()
                    chronometer.stop()
                    startStop.text = "시작"
                    lapReset.text = "리셋"
                    lapState = false
                }

                false -> {
                    chronometer.base = SystemClock.elapsedRealtime() + time
                    chronometer.start()
                    startStop.text = "정지"
                    lapReset.text = "랩"
                    lapReset.isEnabled = true
                    lapState = true
                }
            }

            startState = !startState
        }

        lapReset.setOnClickListener {
            when(lapState){
                true -> {
                    lapTimeArrayList.add(createLapTimeData(chronometer.text.toString()))
                    recyclerAdapter.setData(lapTimeArrayList)
                    recyclerView.scrollToPosition(lapTimeArrayList.size - 1)
                }
                false -> {
                    time = 0L
                    chronometer.base = SystemClock.elapsedRealtime()
                    chronometer.stop()
                    lapReset.isEnabled = false
                    lapReset.text = "랩"
                    lapTimeArrayList.clear()
                    recyclerAdapter.setData(lapTimeArrayList)
                    lapTimeIndex = 1
                }
            }
        }

        return binding!!.root
    }

    private fun createLapTimeData(lapTime : String) : LapData{
        val time = lapTime.split(":").map { it.toInt() }

        return LapData(lapTimeIndex++, time[0], time[1])
    }
}