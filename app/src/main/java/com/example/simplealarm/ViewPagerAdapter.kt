package com.example.simplealarm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.simplealarm.fragment.AlarmFragment
import com.example.simplealarm.fragment.StopWatchFragment
import com.example.simplealarm.fragment.StopWatchFragmentV2
import com.example.simplealarm.fragment.WorldTimeFragment

class ViewPagerAdapter(fragmentManager : FragmentManager, lifecycle : Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> AlarmFragment()
            1 -> WorldTimeFragment()
            2 -> StopWatchFragmentV2()
            else -> AlarmFragment()
        }
    }
}