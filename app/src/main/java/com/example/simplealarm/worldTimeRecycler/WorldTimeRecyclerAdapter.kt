package com.example.simplealarm.worldTimeRecycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplealarm.databinding.WorldTimeRecyclerItemBinding
import java.text.SimpleDateFormat
import java.util.*

class WorldTimeRecyclerAdapter(recyclerInterface : WorldTimeRecyclerClickCallback) : RecyclerView.Adapter<WorldTimeRecyclerAdapter.WorldTimeViewHolder>() {
    private var customInterface : WorldTimeRecyclerClickCallback = recyclerInterface
    private var countryList = emptyList<CountryTime>()

    class WorldTimeViewHolder(private val binding : WorldTimeRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : CountryTime, mInterface : WorldTimeRecyclerClickCallback){
            val df = SimpleDateFormat("yyyy/MM/dd HH:mm")
            val timeZone = TimeZone.getTimeZone(data.timeZone)
            df.timeZone = timeZone

            binding.time.text = "${data.location}\n${df.format(Date())}"

            binding.delete.setOnClickListener {
                mInterface.onClickDelete(data.location)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldTimeViewHolder {
        val binding = WorldTimeRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorldTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorldTimeViewHolder, position: Int) {
        holder.bind(countryList[position], customInterface)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(country : List<CountryTime>){
        countryList = country
        notifyDataSetChanged()
    }
}