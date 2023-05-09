package com.example.simplealarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.simplealarm.repository.RepositoryInterface
import com.example.simplealarm.room.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(private val repository: RepositoryInterface) : androidx.lifecycle.ViewModel() {
    private val _alarmData : LiveData<List<Alarm>> = repository.alarmData.asLiveData()
    val alarmData get() = _alarmData

    fun addAlarm(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlarm(alarm)
        }
    }

    fun deleteAlarm(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(id)
        }
    }
}