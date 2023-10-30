package com.example.simplealarm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplealarm.lapTimeRecycler.StopWatchTimeData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timer

@HiltViewModel
class StopWatchViewModel @Inject constructor() : ViewModel() {
    private val _stopWatchData : MutableLiveData<StopWatchTimeData> by lazy {
        MutableLiveData<StopWatchTimeData>()
    }
    val stopWatchData get() = _stopWatchData

    private var _startState = false
    val startState get() = _startState

    private var _stopWatchState = false
    val stopWatchState get() = _stopWatchState

    // 스톱워치 데이터 - 만약 SharedPreference 저장된 데이터가 없을경우 초기상태로 사용
    private var stopWatchTime = StopWatchTimeData(0, 0, 0, 0)

    // 스톱워치 기능을 수행하는 Timer 객체
    private lateinit var timerTask : Timer

    fun setStopWatchData(data : StopWatchTimeData){
        stopWatchTime = data
        _stopWatchData.postValue(data)
    }

    fun changeStopWatchState(state : Boolean){
        _stopWatchState = state

        // lapState Flag 에 따라 시간을 증가시킴
        if(_stopWatchState){
            timerTask = timer(period = 10){
                stopWatchTime.milSec++
                stopWatchTime.calculateTime()
                _stopWatchData.postValue(stopWatchTime)
            }
        } else {
            timerTask.cancel()
        }
    }

    fun changeStartState(state : Boolean){
        _startState = state
    }

    fun reset(){
        _stopWatchData.value!!.reset()
        stopWatchTime.reset()
    }
}