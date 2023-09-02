package com.example.simplealarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simplealarm.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val tabName = listOf("알람", "세계 시간", "스톱워치")
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        viewPager.adapter =  ViewPagerAdapter(supportFragmentManager, lifecycle)

        // TabLayout 에 각 Fragment 이름을 지정
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            when(position){
                0 -> tab.text = tabName[0]
                1 -> tab.text = tabName[1]
                2 -> tab.text = tabName[2]
            }
        }.attach()
    }
}