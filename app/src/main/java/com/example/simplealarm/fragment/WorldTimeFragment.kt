package com.example.simplealarm.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplealarm.worldTimeRecycler.CountryTime
import com.example.simplealarm.R
import com.example.simplealarm.worldTimeRecycler.WorldTimeRecyclerAdapter
import com.example.simplealarm.databinding.WorldTimeFragmentBinding
import com.example.simplealarm.dialog.CountryAddDialog
import com.example.simplealarm.dialog.CountryAddInterface
import com.example.simplealarm.worldTimeRecycler.WorldTimeRecyclerClickCallback
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import kotlin.concurrent.timer

@AndroidEntryPoint
class WorldTimeFragment : Fragment(), CountryAddInterface, WorldTimeRecyclerClickCallback {
    private var binding: WorldTimeFragmentBinding? = null
    private val recyclerAdapter = WorldTimeRecyclerAdapter(this)
    private val countryData = ArrayList<CountryTime>()
    private lateinit var country: Array<String>
    private val df = SimpleDateFormat("ss")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WorldTimeFragmentBinding.inflate(inflater, container, false)

        country = resources.getStringArray(R.array.data)

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

    override fun onPositive(index: Int) {
        val timezone = when (index) {
            0 -> "Asia/Seoul"
            1 -> "US/Eastern"
            2 -> "Europe/London"
            else -> ""
        }

        if (timezone != "") {
            countryData.add(CountryTime(country[index], timezone))
            recyclerAdapter.setData(countryData)
        }
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
        recyclerAdapter.setData(countryData)
    }
}
