package com.example.graduationproject.presentation.challenges

sealed interface ChallengeViewState <out T> {

    data class Success<T> (val data: T) : ChallengeViewState<T>
    data class Failure(val message: String) : ChallengeViewState<Nothing>
    data object Loading : ChallengeViewState<Nothing>
}

