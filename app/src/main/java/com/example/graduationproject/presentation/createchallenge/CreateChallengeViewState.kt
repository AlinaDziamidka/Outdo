package com.example.graduationproject.presentation.createchallenge

import com.example.graduationproject.presentation.creategroup.CreateGroupViewState

class CreateChallengeViewState <out T> {

    data class Success<T>(val data: T) : CreateGroupViewState<T>
    data class Failure(val message: String) : CreateGroupViewState<Nothing>
    data object Loading : CreateGroupViewState<Nothing>
}