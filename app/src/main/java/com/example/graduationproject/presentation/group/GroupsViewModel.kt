package com.example.graduationproject.presentation.group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.GroupParticipants
import com.example.graduationproject.domain.usecase.FetchUserGroupsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    context: Application,
    private val fetchUserGroupsUseCase: FetchUserGroupsUseCase
) : AndroidViewModel(context) {

    private val _viewState =
        MutableStateFlow<GroupViewState<MutableList<GroupParticipants>>>(GroupViewState.Loading)
    val viewState: StateFlow<GroupViewState<MutableList<GroupParticipants>>> = _viewState

    fun setUpUserGroups(userId: String) {
        viewModelScope.launch {
            fetchUserGroupsUseCase(
                FetchUserGroupsUseCase.Params(
                    userId
                )
            )
                .onStart {
                    _viewState.value = GroupViewState.Loading
                }.catch {
                    _viewState.value =
                        GroupViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { groupParticipants ->
                    _viewState.value = GroupViewState.Success(groupParticipants)
                }
        }
    }
}

