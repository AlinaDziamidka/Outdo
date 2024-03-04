package com.example.graduationproject.presentation.signin

sealed interface SignInViewState {
    data object Success : SignInViewState
    data class Failure(val message: String) : SignInViewState
    data object Loading : SignInViewState
    data object Idle : SignInViewState
}