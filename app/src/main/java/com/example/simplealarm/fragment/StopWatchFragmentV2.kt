package com.example.simplealarm.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplealarm.StopWatchViewModel
import com.example.simplealarm.databinding.StopwatchFargmentBinding
import com.example.simplealarm.lapTimeRecycler.LapDataV2
import com.example.simplealarm.lapTimeRecycler.LapTimeRecyclerAdapterV2
import com.example.simplealarm.lapTimeRecycler.StopWatchTimeData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList

class StopWatchFragmentV2 : Fragment() {
    private var binding : StopwatchFargmentBinding? = null
    private val recyclerAdapter = LapTimeRecyclerAdapterV2()
    private var lapTimeArrayList = ArrayList<LapDataV2>()
    private var lapTimeIndex = 1
    private val viewModel : StopWatchViewModel by viewModels()
    private lateinit var stopWatchData : StopWatchTimeData
    private lateinit var stopWatchSharedPreferences : SharedPreferences
    private lateinit var lapTimeWatchSharedPreferences : SharedPreferences

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
        loadSharedPreference()

        recyclerAdapter.setHasStableIds(true)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter

        // View Model의 스톱워치 데이터를 observe하여 값 변경시 View를 갱신
        viewModel.stopWatchData.observe(viewLifecycleOwner){
            binding!!.stopWatch = it
        }
        viewModel.setStopWatchData(stopWatchData)

        startStop.setOnClickListener {
            when(viewModel.startState){
                true -> {
                    startStop.text = "시작"
                    lapReset.text = "리셋"
                    viewModel.changeLapState(false)
                }

                false -> {
                    startStop.text = "정지"
                    lapReset.text = "랩"
                    lapReset.isEnabled = true
                    viewModel.changeLapState(true)
                }
            }
            viewModel.changeStartState(!viewModel.startState)
        }

        lapReset.setOnClickListener {
            when(viewModel.lapState){
                true -> {
                    lapTimeArrayList.add(createLapTimeData())
                    recyclerAdapter.setData(lapTimeArrayList)
                    recyclerView.scrollToPosition(lapTimeArrayList.size - 1)
                }
                false -> reset()
            }
        }

        // 기존 값이 있는지 확인, 있다면 리셋 기능 활성화
        if(lapTimeArrayList.isNotEmpty() ||
            viewModel.stopWatchData.value?.hour != 0 ||
            viewModel.stopWatchData.value?.minute != 0 ||
            viewModel.stopWatchData.value?.second != 0 ||
            viewModel.stopWatchData.value?.milSec != 0){
            lapReset.text = "리셋"
            lapReset.isEnabled = true
            viewModel.changeLapState(false)
        }

        return binding!!.root
    }

    private fun createLapTimeData() : LapDataV2{
        return LapDataV2(lapTimeIndex++, stopWatchData.hour, stopWatchData.minute, stopWatchData.second, stopWatchData.milSec)
    }

    private fun reset(){
        binding!!.stopWatch = stopWatchData
        binding!!.lapReset.isEnabled = false
        binding!!.lapReset.text = "랩"
        viewModel.reset()
        lapTimeArrayList.clear()
        recyclerAdapter.setData(lapTimeArrayList)
        lapTimeIndex = 1
        saveSharedPreference()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // onAttach 단계에서 SharedPreference에 저장된 항목을 불러옴
        stopWatchSharedPreferences = requireActivity().getSharedPreferences("stopWatchData", Context.MODE_PRIVATE)
        lapTimeWatchSharedPreferences = requireActivity().getSharedPreferences("lapTime", Context.MODE_PRIVATE)
        loadSharedPreference()
    }

    override fun onPause() {
        super.onPause()
        saveSharedPreference()
    }

    private fun saveSharedPreference(){
        // 데이터를 JSON 형식으로 변환 후 저장
        val editStopWatch = stopWatchSharedPreferences.edit()
        val editLapTime = lapTimeWatchSharedPreferences.edit()
        val gson = GsonBuilder().create()

        editStopWatch.putString("stopWatchData", gson.toJson(stopWatchData, StopWatchTimeData::class.java))
        editStopWatch.apply()

        editLapTime.putString("lapTime", gson.toJson(lapTimeArrayList))
        editLapTime.apply()
    }

    private fun loadSharedPreference(){
        // 데이터를 JSON 형식에서 ArrayList 로 변환
        val loadStopWatch = stopWatchSharedPreferences.getString("stopWatchData", "")
        val stopWatchTypeData = object : TypeToken<StopWatchTimeData>(){}.type

        val loadLapTime = lapTimeWatchSharedPreferences.getString("lapTime", "")
        val lapTimeTypeData = object : TypeToken<ArrayList<LapDataV2>>(){}.type

        if(loadStopWatch != "")
            stopWatchData = Gson().fromJson(loadStopWatch, stopWatchTypeData)

        if(loadLapTime != ""){
            lapTimeArrayList = Gson().fromJson(loadLapTime, lapTimeTypeData)
            lapTimeIndex = lapTimeArrayList.size + 1
            recyclerAdapter.setData(lapTimeArrayList)
        }
    }
}