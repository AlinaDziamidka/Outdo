package com.example.graduationproject.presentation.group

sealed interface GroupViewState <out T> {

    data class Success<T> (val data: T) : GroupViewState<T>
    data class Failure(val message: String) : GroupViewState<Nothing>
    data object Loading : GroupViewState<Nothing>
}

