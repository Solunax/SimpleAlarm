package com.example.simplealarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.simplealarm.room.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(application: Application, private val repository: Repository) : AndroidViewModel(application){
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