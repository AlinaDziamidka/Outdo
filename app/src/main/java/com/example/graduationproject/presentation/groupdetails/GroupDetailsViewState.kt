package com.example.graduationproject.presentation.groupdetails

import com.example.graduationproject.presentation.group.GroupViewState

sealed interface GroupDetailsViewState <out T> {

        data class Success<T> (val data: T) :GroupDetailsViewState<T>
        data class Failure(val message: String) : GroupDetailsViewState<Nothing>
        data object Loading : GroupDetailsViewState<Nothing>
}