package com.example.simplealarm

interface RecyclerClickCallback {
    fun onClick(position : Int, state : Boolean)
    fun onClickDelete(position: Int)
}