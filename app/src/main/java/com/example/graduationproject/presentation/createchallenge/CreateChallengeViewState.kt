package com.example.graduationproject.presentation.createchallenge

interface CreateChallengeViewState <out T> {
    data class Success<T>(val data: T) : CreateChallengeViewState<T>
    data class Failure(val message: String) : CreateChallengeViewState<Nothing>
    data object Loading : CreateChallengeViewState<Nothing>
}