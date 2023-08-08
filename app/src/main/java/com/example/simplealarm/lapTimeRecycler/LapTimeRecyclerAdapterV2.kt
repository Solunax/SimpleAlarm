package com.example.simplealarm.lapTimeRecycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplealarm.databinding.LaptimeRecyclerItemBinding
import com.example.simplealarm.databinding.LaptimeRecyclerItemV2Binding

class LapTimeRecyclerAdapterV2 : RecyclerView.Adapter<LapTimeRecyclerAdapterV2.LapTimeViewHolder>() {
    private var lapTimeData = emptyList<LapDataV2>()

    class LapTimeViewHolder(private val binding : LaptimeRecyclerItemV2Binding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: LapDataV2){
            binding.lapTime = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapTimeViewHolder {
        val binding =
            LaptimeRecyclerItemV2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LapTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LapTimeViewHolder, position: Int) {
        holder.bind(lapTimeData[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return lapTimeData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data : ArrayList<LapDataV2>){
        lapTimeData = data
        notifyDataSetChanged()
    }
}