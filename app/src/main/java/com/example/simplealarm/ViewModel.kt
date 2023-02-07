package com.example.simplealarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.simplealarm.room.Alarm
import com.example.simplealarm.room.AppDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(application: Application) : AndroidViewModel(application){
    private val _alarmData : LiveData<List<Alarm>>
    val alarmData get() = _alarmData
    private val repository : Repository

    init {
        repository = Repository(AppDataBase.getInstance(application))
        _alarmData = repository.alarmData.asLiveData()
    }

    fun addAlarm(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(alarm)
        }
    }
}