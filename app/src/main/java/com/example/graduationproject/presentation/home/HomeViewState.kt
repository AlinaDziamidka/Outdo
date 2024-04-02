package com.example.graduationproject.presentation.home

sealed interface HomeViewState {

    data object Success : HomeViewState
    data class Failure(val message: String) : HomeViewState
    data object Loading : HomeViewState
    data object Idle : HomeViewState
}