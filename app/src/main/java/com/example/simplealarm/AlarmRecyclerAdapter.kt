package com.example.simplealarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.simplealarm.databinding.AlarmRecyclerItemBinding
import com.example.simplealarm.dialog.TimePicekrInterface
import com.example.simplealarm.room.Alarm
import java.util.*

class AlarmRecyclerAdapter(recyclerInterface : RecyclerClickCallback) : RecyclerView.Adapter<AlarmRecyclerAdapter.AlarmViewHolder>(){
    private var customInterface : RecyclerClickCallback = recyclerInterface
    private var alarmList = emptyList<Alarm>()

    class AlarmViewHolder(private val binding : AlarmRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(alarm : Alarm, mInterface : RecyclerClickCallback){
            binding.alarm = alarm

            // 알람 스위치
            binding.alarmSwitch.setOnCheckedChangeListener { compoundButton, check ->
                mInterface.onClick(alarm.alarmID, check)
            }

            binding.delete.setOnClickListener {
                mInterface.onClickDelete(alarm.alarmID)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = AlarmRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(alarmList[position], customInterface)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(alarms : List<Alarm>){
        alarmList = alarms
        notifyDataSetChanged()
    }
}