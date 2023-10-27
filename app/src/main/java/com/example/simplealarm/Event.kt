package com.example.simplealarm

// Event 관리용 Class
open class Event<out T>(private val content: T) {
    private var hasBeenHandle = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandle) {
            null
        } else {
            hasBeenHandle = true
            content
        }
    }
}