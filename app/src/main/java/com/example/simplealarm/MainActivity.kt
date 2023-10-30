package com.example.simplealarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.simplealarm.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val tabName = listOf("알람", "세계 시간", "스톱워치")
    private lateinit var binding: ActivityMainBinding
    private var lastBackPressedTime = 0L

    // 뒤로가기 버튼을 두번 클릭시 앱을 종료하는 기능을 수행하는 콜백
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(System.currentTimeMillis() > lastBackPressedTime + 2000) {
                lastBackPressedTime = System.currentTimeMillis()
                Toast.makeText(this@MainActivity, "뒤로가기 버튼을 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        // TabLayout 에 각 Fragment 이름을 지정
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = tabName[0]
                1 -> tab.text = tabName[1]
                2 -> tab.text = tabName[2]
            }
        }.attach()

        // 뒤로가기 버튼 클릭 콜백 연결
        onBackPressedDispatcher.addCallback(this,backPressedCallback)
    }
}