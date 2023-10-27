package com.example.simplealarm.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplealarm.dialog.TimePickerInterface
import com.example.simplealarm.dialog.TimeSetDialog
import com.example.simplealarm.room.Alarm
import androidx.fragment.app.viewModels
import com.example.simplealarm.alarmRecycler.AlarmRecyclerAdapter
import com.example.simplealarm.AlarmViewModel
import com.example.simplealarm.alarmRecycler.AlarmRecyclerClickCallback
import com.example.simplealarm.databinding.AlarmFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
// RecyclerView 및 TimePicker Dialog 에서 발생한 Click Event 를 처리하기 위한 Interface 구현
class AlarmFragment : Fragment(), TimePickerInterface, AlarmRecyclerClickCallback {
    private var binding : AlarmFragmentBinding? = null
    private val viewModel : AlarmViewModel by viewModels()
    private val recyclerAdapter = AlarmRecyclerAdapter(this)
    private var alarmData : List<Alarm> = emptyList()
    private lateinit var calendar : Calendar
    private var alarmPermission = false
    private var notificationPermission = false
    private val notificationPermissionActivity = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ granted ->
        if(!granted)
            Toast.makeText(context, "권한 설정을 거부하여 알람을 활성할 수 없습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AlarmFragmentBinding.inflate(inflater, container, false)


        val setTime = binding!!.timeSet
        val recyclerView = binding!!.alarmRecycler
        recyclerAdapter.setHasStableIds(true)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter

        viewModel.alarmData.observe(viewLifecycleOwner){
            alarmData = it
            recyclerAdapter.setData(alarmData)
            viewModel.alarmValidateCheck(requireContext(), requireActivity())
        }

        viewModel.event.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { code ->
                when(code){
                    // 알람이 성공적으로 생성됐다는 Event 수신시
                    "Alarm Set Success" -> {
                        Toast.makeText(context, "${viewModel.calendar.get(Calendar.MONTH) + 1}월 ${calendar.get(
                            Calendar.DATE)}일\n${calendar.get(Calendar.HOUR_OF_DAY)}시 ${calendar.get(Calendar.MINUTE)}분에 알람이 울립니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        setTime.setOnClickListener {
            val timePickerDialog = TimeSetDialog(requireContext(), this)
            timePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            timePickerDialog.show()
        }

        calendar = viewModel.calendar

        return binding!!.root
    }

    private fun permissionCheck() : Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            // 알람 권한 설정 여부(상위 버전만 해당)
            if(viewModel.alarmManager.canScheduleExactAlarms()){
                alarmPermission = true
            } else {
                alarmPermission = false
                requestAlarmPermission()
            }

            // notification 권한 설정 여부
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
                notificationPermission = true
            } else {
                requestNotificationPermission()
            }

            return alarmPermission && notificationPermission
        } else {
            return true
        }
    }

    // 권한이 거부된 상태면 알람 권한 설정 화면 띄우기
    private fun requestAlarmPermission() {
        Toast.makeText(requireContext(), "알람 권한 설정이 필요합니다.", Toast.LENGTH_SHORT).show()
        Intent().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                action = ACTION_REQUEST_SCHEDULE_EXACT_ALARM
        }.also {
            startActivity(it)
        }
    }

    // 권한이 거부된 상태면 notification 권한 설정 화면 띄우기
    private fun requestNotificationPermission(){
        // 만약 사용자가 권한 체크를 거부한 적이 있을 경우 직접 권한을 설정하게 끔 설정
        // 사용자가 다시 권한 요청이 뜨는 것을 거부한 경우도 있긴 하지만 두가지 경우만 체크
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.POST_NOTIFICATIONS)){
            Toast.makeText(context, "과거에 알람 권한 설정을 거부하여 직접 승인해야 합니다.", Toast.LENGTH_SHORT).show()
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", requireContext().packageName, null))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).also {
                    startActivity(it)
                }
        }else{
            Toast.makeText(context, "알람 권한 설정이 필요합니다.", Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                notificationPermissionActivity.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", requireContext().packageName, null))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).also {
                        startActivity(it)
                    }
            }
        }
    }

    // Custom Dialog 의 Interface 구현 메소드
    override fun onPositive(hour: Int, minute: Int) {
        val alarm = Alarm(0, hour, minute, false)
        viewModel.addAlarm(alarm)
    }

    // RecyclerView 에서 발생한 이벤트 처리를 위한 Interface 구현 메소드
    // RecyclerView 에서 클릭 이벤트 발생시(CheckBox)
    override fun onClickSetAlarm(position: Int, state: Boolean){
        // 알람과 notification 권한이 모두 허용된 상태일 경우 알람 켜기
        if(permissionCheck()){
            viewModel.setAlarm(position, state)
        } else {
            // RecyclerView의 Switch 상태를 원래 상태로 돌림
            recyclerAdapter.setData(alarmData)
        }
    }

    // RecyclerView 에서 발생한 이벤트 처리를 위한 Interface 구현 메소드
    override fun onClickDeleteAlarm(index : Int) {
        viewModel.deleteAlarm(index)
    }
}