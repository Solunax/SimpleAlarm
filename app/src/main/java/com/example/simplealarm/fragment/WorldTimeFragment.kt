package com.example.simplealarm.fragment

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplealarm.worldTimeRecycler.CountryTime
import com.example.simplealarm.R
import com.example.simplealarm.worldTimeRecycler.WorldTimeRecyclerAdapter
import com.example.simplealarm.databinding.WorldTimeFragmentBinding
import com.example.simplealarm.dialog.CountryAddDialog
import com.example.simplealarm.dialog.CountryAddInterface
import com.example.simplealarm.worldTimeRecycler.WorldTimeRecyclerClickCallback
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import kotlin.concurrent.timer

@AndroidEntryPoint
class WorldTimeFragment : Fragment(), CountryAddInterface, WorldTimeRecyclerClickCallback {
    private var binding: WorldTimeFragmentBinding? = null
    private val recyclerAdapter = WorldTimeRecyclerAdapter(this)
    private var countryData = ArrayList<CountryTime>()
    private lateinit var country: Array<String>
    private lateinit var timeZoneData : Array<String>
    private val df = SimpleDateFormat("ss")
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WorldTimeFragmentBinding.inflate(inflater, container, false)

        country = resources.getStringArray(R.array.country)
        timeZoneData = resources.getStringArray(R.array.timezone)

        val recyclerView = binding!!.worldTimeRecycler
        recyclerAdapter.setHasStableIds(true)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter

        val addCountry = binding!!.addCountry
        addCountry.setOnClickListener {
            val addCountryDialog = CountryAddDialog(requireContext(), this)
            addCountryDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addCountryDialog.show()
        }

        timer(period = 1000) {
            if (df.format(System.currentTimeMillis()) == "00") {
                requireActivity().runOnUiThread {
                    recyclerAdapter.setData(countryData)
                }
            }
        }

        return binding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireActivity().getSharedPreferences("country", MODE_PRIVATE)
        loadSharedPreference()
    }

    override fun onPositive(index: Int) {
        val timezone = timeZoneData[index]

        var check = true
        for(data in countryData){
            if(data.timeZone == timezone){
                check = false
                break
            }
        }

        if(check){
            if (timezone != "") {
                countryData.add(CountryTime(country[index], timezone))
                recyclerAdapter.setData(countryData)
            }

            saveSharedPreference()
        }else
            Toast.makeText(requireContext(), "이미 존재하는 세계시간입니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onClickDelete(name: String) {
        var index = -1
        for (i in 0 until countryData.size) {
            if (countryData[i].location == name) {
                index = i
                break
            }
        }

        countryData.removeAt(index)
        saveSharedPreference()
        recyclerAdapter.setData(countryData)
    }

    private fun saveSharedPreference(){
        val edit = sharedPreferences.edit()
        val gson = GsonBuilder().create()

        edit.putString("country", gson.toJson(countryData))
        edit.apply()
    }

    private fun loadSharedPreference(){
        val loadData = sharedPreferences.getString("country", "")
        val typeData = object : TypeToken<ArrayList<CountryTime>>(){}.type

        if(loadData != ""){
            countryData = Gson().fromJson(loadData, typeData)
            recyclerAdapter.setData(countryData)
        }
    }
}
