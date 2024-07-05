package com.example.graduationproject.presentation.notifications


interface NotificationsViewState <out T> {

    data class Success<T>(val data: T) : NotificationsViewState<T>
    data class Failure(val message: String) : NotificationsViewState<Nothing>
    data object Loading : NotificationsViewState<Nothing>
}