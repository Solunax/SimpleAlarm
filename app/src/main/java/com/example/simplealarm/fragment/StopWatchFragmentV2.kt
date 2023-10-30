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

        // SharedPreference 에 저장된 값을 불러옴(stopWatchData, lapTime 데이터)
        loadSharedPreference()

        recyclerAdapter.setHasStableIds(true)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter

        // View Model 의 stopWatchData 를 관측해 값 변경시 View 를 갱신
        viewModel.stopWatchData.observe(viewLifecycleOwner){
            binding!!.stopWatch = it
        }

        // SharedPreference 의 stopWatchData 로 viewModel 의 stopWatchData 를 설정
        viewModel.setStopWatchData(stopWatchData)

        // viewModel 의 startState Flag 는 스톱워치가 동작하고있는지 확인하는 Flag
        // 시작, 종료 버튼 클릭시 상태에 맞게 viewModel 의 Flag 를 변경
        // viewModel 은 Flag 의 상태에 따라 스톱워치 동작을 제어함
        startStop.setOnClickListener {
            when(viewModel.startState){
                true -> {
                    startStop.text = "시작"
                    lapReset.text = "리셋"
                    viewModel.changeStopWatchState(false)
                }

                false -> {
                    startStop.text = "정지"
                    lapReset.text = "랩"
                    lapReset.isEnabled = true
                    viewModel.changeStopWatchState(true)
                }
            }
            viewModel.changeStartState(!viewModel.startState)
        }

        // 스톱워치가 활성화되어 있으면 lapTime을 저장
        // 스톱워치가 비활성화되어 있고, 저장된 lapTime이 있다면 Reset기능을 수행함
        lapReset.setOnClickListener {
            when(viewModel.stopWatchState){
                true -> {
                    lapTimeArrayList.add(createLapTimeData())
                    recyclerAdapter.setData(lapTimeArrayList)
                    recyclerView.scrollToPosition(lapTimeArrayList.size - 1)
                }
                false -> reset()
            }
        }

        // 기존 값이 있는지 확인, 있다면 리셋 기능 활성화
        // 앱 실행 후 최초 1회 동작(앱 종료 이전에 저장된 값 초기화시)
        if(lapTimeArrayList.isNotEmpty() ||
            viewModel.stopWatchData.value?.hour != 0 ||
            viewModel.stopWatchData.value?.minute != 0 ||
            viewModel.stopWatchData.value?.second != 0 ||
            viewModel.stopWatchData.value?.milSec != 0){
            lapReset.text = "리셋"
            lapReset.isEnabled = true
        }

        return binding!!.root
    }

    // lapTime 데이터를 저장할 객체를 생성하여 반환하는 함수
    // lapTime 인덱스는 객체 생성 후 증가함
    private fun createLapTimeData() : LapDataV2{
        return LapDataV2(lapTimeIndex++, stopWatchData.hour, stopWatchData.minute, stopWatchData.second, stopWatchData.milSec)
    }

    // stopWatch Data 및 lapTime 데이터 초기화
    // 초기화 데이터를 SharedPreference 에 저장
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

    // onAttach 단계에서 SharedPreference에 저장된 항목을 불러옴
    override fun onAttach(context: Context) {
        super.onAttach(context)
        stopWatchSharedPreferences = requireActivity().getSharedPreferences("stopWatchData", Context.MODE_PRIVATE)
        lapTimeWatchSharedPreferences = requireActivity().getSharedPreferences("lapTime", Context.MODE_PRIVATE)
    }

    override fun onPause() {
        super.onPause()
        saveSharedPreference()
    }

    // stopWatchData, lapTimeArrayList 를 JSON 형식으로 변환 후 저장
    private fun saveSharedPreference(){
        val editStopWatch = stopWatchSharedPreferences.edit()
        val editLapTime = lapTimeWatchSharedPreferences.edit()
        val gson = GsonBuilder().create()

        editStopWatch.putString("stopWatchData", gson.toJson(stopWatchData, StopWatchTimeData::class.java))
        editStopWatch.apply()

        editLapTime.putString("lapTime", gson.toJson(lapTimeArrayList))
        editLapTime.apply()
    }

    // SharedPreference 에 저장된 stopWatchData, lapTimeData 를 JSON 형식에서 ArrayList 로 변환
    private fun loadSharedPreference(){
        val loadStopWatch = stopWatchSharedPreferences.getString("stopWatchData", "")
        val stopWatchTypeData = object : TypeToken<StopWatchTimeData>(){}.type

        val loadLapTime = lapTimeWatchSharedPreferences.getString("lapTime", "")
        val lapTimeTypeData = object : TypeToken<ArrayList<LapDataV2>>(){}.type


        // 만약 기존에 저장된 데이터가 없다면 기본값으로 초기화함
        stopWatchData = if(loadStopWatch != ""){
            Gson().fromJson(loadStopWatch, stopWatchTypeData)
        } else {
            StopWatchTimeData(0, 0, 0, 0)
        }

        if(loadLapTime != ""){
            lapTimeArrayList = Gson().fromJson(loadLapTime, lapTimeTypeData)
            lapTimeIndex = lapTimeArrayList.size + 1
            recyclerAdapter.setData(lapTimeArrayList)
        }
    }
}