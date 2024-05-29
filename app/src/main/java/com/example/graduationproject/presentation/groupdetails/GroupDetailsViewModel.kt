package com.example.graduationproject.presentation.groupdetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.usecase.FetchGroupChallengesUseCase
import com.example.graduationproject.domain.usecase.FetchGroupNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
    context: Application,
    private val fetchGroupChallengesUseCase: FetchGroupChallengesUseCase,
    private val fetchGroupNameUseCase: FetchGroupNameUseCase
) :
    AndroidViewModel(context) {

    private val _viewStateChallenges =
        MutableStateFlow<GroupDetailsViewState<MutableList<Challenge>>>(GroupDetailsViewState.Loading)
    val viewStateChallenges: StateFlow<GroupDetailsViewState<MutableList<Challenge>>> =
        _viewStateChallenges

    private val _viewStateChallengesHistory =
        MutableStateFlow<GroupDetailsViewState<MutableList<Challenge>>>(GroupDetailsViewState.Loading)
    val viewStateChallengesHistory: StateFlow<GroupDetailsViewState<MutableList<Challenge>>> =
        _viewStateChallengesHistory

    private val _viewStateCurrentGroup =
        MutableStateFlow<GroupDetailsViewState<Group>>(GroupDetailsViewState.Loading)
    val viewStateCurrentGroup: StateFlow<GroupDetailsViewState<Group>> =
        _viewStateCurrentGroup

    fun setCurrentGroup(groupId: String) {
        viewModelScope.launch {
            fetchGroupNameUseCase(
                FetchGroupNameUseCase.Params(groupId)
            )
                .onStart {
                    _viewStateCurrentGroup.value = GroupDetailsViewState.Loading
                }.catch {
                    Log.e(
                        "GroupDetailsViewModel",
                        "Fetching current group: Error - ${it.message}"
                    )
                    _viewStateCurrentGroup.value =
                        GroupDetailsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { group ->
                    Log.d("GroupDetailsViewModel", "Fetching group: Success")
                    _viewStateCurrentGroup.value = GroupDetailsViewState.Success(group)
                }
        }
    }

    fun setUpChallenges(groupId: String) {
        viewModelScope.launch {
            Log.d("GroupDetailsViewModel", "Fetching challenges $groupId")
            fetchGroupChallengesUseCase(
                FetchGroupChallengesUseCase.Params(
                    groupId, ChallengeStatus.UNFINISHED
                )
            )
                .onStart {
                    Log.d("GroupDetailsViewModel", "Fetching challenges: Loading state")
                    _viewStateChallenges.value = GroupDetailsViewState.Loading
                }.catch {
                    Log.e(
                        "GroupDetailsViewModel",
                        "Fetching challenges: Error - ${it.message}"
                    )
                    _viewStateChallenges.value =
                        GroupDetailsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { challenges ->
                    Log.d("GroupDetailsViewModel", "Fetching challenges: Success")
                    _viewStateChallenges.value = GroupDetailsViewState.Success(challenges)
                }
        }
    }

    fun setUpChallengesHistory(groupId: String) {
        viewModelScope.launch {
            Log.d("GroupDetailsViewModel", "Fetching challenges history$groupId")
            fetchGroupChallengesUseCase(
                FetchGroupChallengesUseCase.Params(
                    groupId, ChallengeStatus.FINISHED
                )
            )
                .onStart {
                    Log.d("GroupDetailsViewModel", "Fetching challenges history: Loading state")
                    _viewStateChallengesHistory.value = GroupDetailsViewState.Loading
                }.catch {
                    Log.e(
                        "GroupDetailsViewModel",
                        "Fetching challenges: Error - ${it.message}"
                    )
                    _viewStateChallengesHistory.value =
                        GroupDetailsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { challenges ->
                    Log.d("GroupDetailsViewModel", "Fetching challenges history: Success")
                    _viewStateChallengesHistory.value = GroupDetailsViewState.Success(challenges)
                }
        }
    }
}