package com.example.graduationproject.presentation.groupparticipants

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.usecase.FetchGroupChallengesUseCase
import com.example.graduationproject.domain.usecase.FetchGroupNameUseCase
import com.example.graduationproject.domain.usecase.FetchGroupParticipantsUseCase
import com.example.graduationproject.presentation.groupdetails.GroupDetailsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupParticipantsViewModel @Inject constructor(
    context: Application,
    private val fetchGroupNameUseCase: FetchGroupNameUseCase,
    private val fetchGroupParticipantsUseCase: FetchGroupParticipantsUseCase
) :
    AndroidViewModel(context) {

    private val _viewStateCurrentGroup =
        MutableStateFlow<GroupParticipantsViewState<Group>>(GroupParticipantsViewState.Loading)
    val viewStateCurrentGroup: StateFlow<GroupParticipantsViewState<Group>> =
        _viewStateCurrentGroup

    private val _viewStateParticipants =
        MutableStateFlow<GroupParticipantsViewState<MutableList<UserProfile>>>(
            GroupParticipantsViewState.Loading
        )
    val viewStateParticipants: StateFlow<GroupParticipantsViewState<MutableList<UserProfile>>> =
        _viewStateParticipants

    fun setCurrentGroup(groupId: String) {
        viewModelScope.launch {
            fetchGroupNameUseCase(
                FetchGroupNameUseCase.Params(groupId)
            )
                .onStart {
                    _viewStateCurrentGroup.value = GroupParticipantsViewState.Loading
                }.catch {
                    Log.e(
                        "GroupParticipantsViewModel",
                        "Fetching current group: Error - ${it.message}"
                    )
                    _viewStateCurrentGroup.value =
                        GroupParticipantsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { group ->
                    Log.d("GroupParticipantsViewModel", "Fetching group: Success")
                    _viewStateCurrentGroup.value = GroupParticipantsViewState.Success(group)
                }
        }
    }

    fun setUpGroupParticipants(groupId: String) {
        viewModelScope.launch {
            Log.d("GroupDetailsViewModel", "Fetching challenges $groupId")
            fetchGroupParticipantsUseCase(
                FetchGroupParticipantsUseCase.Params(
                    groupId
                )
            )
                .onStart {
                    Log.d("GroupParticipantsViewModel", "Fetching participants: Loading state")
                    _viewStateParticipants.value = GroupParticipantsViewState.Loading
                }.catch {
                    Log.e(
                        "GroupParticipantsViewModel",
                        "Fetching participants: Error - ${it.message}"
                    )
                    _viewStateParticipants.value =
                        GroupParticipantsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { participants ->
                    Log.d("GroupParticipantsViewModel", "Fetching participants: Success")
                    _viewStateParticipants.value = GroupParticipantsViewState.Success(participants)
                }
        }
    }
}