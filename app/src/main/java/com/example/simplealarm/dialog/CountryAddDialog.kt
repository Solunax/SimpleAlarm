package com.example.simplealarm.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.example.simplealarm.databinding.CountryAddDialogBinding

class CountryAddDialog (context : Context, mInterface : CountryAddInterface) : Dialog(context) {
    private var customInterface : CountryAddInterface = mInterface
    private lateinit var binding : CountryAddDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CountryAddDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val listview = binding.listView
        listview.setOnItemClickListener { adapterView, view, i, l ->
            customInterface.onPositive(i)
            dismiss()
        }
    }
}