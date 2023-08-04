package com.example.simplealarm.lapTimeRecycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplealarm.databinding.LaptimeRecyclerItemBinding

class LapTimeRecyclerAdapter : RecyclerView.Adapter<LapTimeRecyclerAdapter.LapTimeViewHolder>() {
    private var lapTimeData = emptyList<LapData>()

    class LapTimeViewHolder(private val binding : LaptimeRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: LapData){
            binding.lapTime = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapTimeViewHolder {
        val binding =
            LaptimeRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
    fun setData(data : ArrayList<LapData>){
        lapTimeData = data
        notifyDataSetChanged()
    }
}