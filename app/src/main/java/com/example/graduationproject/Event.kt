package com.example.graduationproject

sealed class Event<out T> {
    data class Success<out T>(val data: T) : Event<T>()
    data class Failure(val message: String) : Event<Nothing>()
}
