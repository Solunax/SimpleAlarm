package com.example.simplealarm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplealarm.databinding.StopwatchFargmentBinding
import com.example.simplealarm.lapTimeRecycler.LapDataV2
import com.example.simplealarm.lapTimeRecycler.LapTimeRecyclerAdapterV2
import com.example.simplealarm.lapTimeRecycler.StopWatchTimeData
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class StopWatchFragmentV2 : Fragment() {
    private var binding : StopwatchFargmentBinding? = null
    private val recyclerAdapter = LapTimeRecyclerAdapterV2()
    private var startState = false
    private var lapState = false
    private var lapTimeArrayList = ArrayList<LapDataV2>()
    private var lapTimeIndex = 1
    private var timerTask : Timer? = null
    private lateinit var stopWatchData : StopWatchTimeData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StopwatchFargmentBinding.inflate(inflater, container, false)

        val startStop = binding!!.startStop
        val lapReset = binding!!.lapReset
        val recyclerView = binding!!.lapTimeRecycler

        stopWatchData = StopWatchTimeData(0, 0, 0, 0)
        binding!!.stopWatch = stopWatchData

        recyclerAdapter.setHasStableIds(true)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter

        startStop.setOnClickListener {
            when(startState){
                true -> {
                    startStop.text = "시작"
                    lapReset.text = "리셋"
                    lapState = false
                }

                false -> {
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
                    lapTimeArrayList.add(createLapTimeData())
                    recyclerAdapter.setData(lapTimeArrayList)
                    recyclerView.scrollToPosition(lapTimeArrayList.size - 1)
                }
                false -> {
                    stopWatchData.reset()
                    binding!!.stopWatch = stopWatchData
                    lapReset.isEnabled = false
                    lapReset.text = "랩"
                    lapTimeArrayList.clear()
                    recyclerAdapter.setData(lapTimeArrayList)
                    lapTimeIndex = 1
                }
            }
        }

        timerTask =  timer(period = 10){
            if(lapState){
                stopWatchData.milSec++
                stopWatchData.calculateTime()
                binding!!.stopWatch = stopWatchData
            }
        }

        return binding!!.root
    }

    private fun createLapTimeData() : LapDataV2{
        return LapDataV2(lapTimeIndex++, stopWatchData.hour, stopWatchData.minute, stopWatchData.second, stopWatchData.milSec)
    }
}