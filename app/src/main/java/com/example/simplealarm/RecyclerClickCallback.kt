package com.example.simplealarm

// RecyclerView 에서 발생하는 Click 이벤트를 처리하기 위해서 선언한 인터페이스
// Activity 에서 구현하여 발생한 이벤트에 맞는 동작을 수행
interface RecyclerClickCallback {
    fun onClick(position : Int, state : Boolean)
    fun onClickDelete(position: Int)
}