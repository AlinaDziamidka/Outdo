package com.example.graduationproject.presentation.challengedetails

import com.example.graduationproject.presentation.groupdetails.GroupDetailsViewState

interface ChallengeDetailsViewState<out T> {

    data class Success<T>(val data: T) : ChallengeDetailsViewState<T>
    data class Failure(val message: String) : ChallengeDetailsViewState<Nothing>
    data object Loading : ChallengeDetailsViewState<Nothing>
}