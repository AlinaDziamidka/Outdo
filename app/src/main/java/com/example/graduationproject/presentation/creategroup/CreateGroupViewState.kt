package com.example.graduationproject.presentation.creategroup

interface CreateGroupViewState<out T> {
    data class Success<T>(val data: T) : CreateGroupViewState<T>
    data class Failure(val message: String) : CreateGroupViewState<Nothing>
    data object Loading : CreateGroupViewState<Nothing>
}