package com.example.simplealarm.lapTimeRecycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplealarm.databinding.LaptimeRecyclerItemBinding

// ChronoMeter를 사용할 때 사용한 Recycler Adapter
// Recycler Adapter V2와 차이는 스톱 워치로 ChronoMeter 를 사용했는지,  Timer 를 사용했는지의 차이
// Timer는 milsec까지 확인 가능
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