package com.example.graduationproject.presentation.groupparticipants

interface GroupParticipantsViewState<out T> {

    data class Success<T>(val data: T) : GroupParticipantsViewState<T>
    data class Failure(val message: String) : GroupParticipantsViewState<Nothing>
    data object Loading : GroupParticipantsViewState<Nothing>
}