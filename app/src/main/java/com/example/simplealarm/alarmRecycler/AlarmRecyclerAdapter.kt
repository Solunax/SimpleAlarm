package com.example.simplealarm.alarmRecycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplealarm.databinding.AlarmRecyclerItemBinding
import com.example.simplealarm.room.Alarm

class AlarmRecyclerAdapter(recyclerInterface : AlarmRecyclerClickCallback) : RecyclerView.Adapter<AlarmRecyclerAdapter.AlarmViewHolder>(){
    private var customInterface : AlarmRecyclerClickCallback = recyclerInterface
    private var alarmList = emptyList<Alarm>()

    class AlarmViewHolder(private val binding : AlarmRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(alarm : Alarm, mInterface : AlarmRecyclerClickCallback){
            binding.alarm = alarm
            // 데이터 추가, 삭제시 활성화된 스위치 버튼이 오작동하는 것을 방지하기 위해 리스너 초기화
            // 리스너를 초기화하고, alarm 데이터의 isOn(활성화 상태)에 맞게 스위치를 변경하도록 수정함
            binding.alarmSwitch.setOnCheckedChangeListener(null)
            binding.alarmSwitch.isChecked = alarm.isOn

            // 알람 스위치
            binding.alarmSwitch.setOnCheckedChangeListener { _, check ->
                mInterface.onClick(alarm.alarmID, check)
            }

            binding.delete.setOnClickListener {
                mInterface.onClickDelete(alarm.alarmID)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding =
            AlarmRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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