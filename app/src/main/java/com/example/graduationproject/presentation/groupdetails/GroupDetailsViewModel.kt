package com.example.graduationproject.presentation.groupdetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.usecase.DeleteUserGroupUseCase
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
    private val deleteUserGroupUseCase: DeleteUserGroupUseCase,
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
                    _viewStateCurrentGroup.value =
                        GroupDetailsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { group ->
                    _viewStateCurrentGroup.value = GroupDetailsViewState.Success(group)
                }
        }
    }

    fun setUpChallenges(groupId: String) {
        viewModelScope.launch {
            fetchGroupChallengesUseCase(
                FetchGroupChallengesUseCase.Params(
                    groupId, ChallengeStatus.UNFINISHED
                )
            )
                .onStart {
                    _viewStateChallenges.value = GroupDetailsViewState.Loading
                }.catch {
                    _viewStateChallenges.value =
                        GroupDetailsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { challenges ->
                    _viewStateChallenges.value = GroupDetailsViewState.Success(challenges)
                }
        }
    }

    fun setUpChallengesHistory(groupId: String) {
        viewModelScope.launch {
            fetchGroupChallengesUseCase(
                FetchGroupChallengesUseCase.Params(
                    groupId, ChallengeStatus.FINISHED
                )
            )
                .onStart {
                    _viewStateChallengesHistory.value = GroupDetailsViewState.Loading
                }.catch {
                    _viewStateChallengesHistory.value =
                        GroupDetailsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { challenges ->
                    _viewStateChallengesHistory.value = GroupDetailsViewState.Success(challenges)
                }
        }
    }

    fun deleteUserGroup(userId: String, groupId: String) {
        viewModelScope.launch {
            runCatching {
                deleteUserGroupUseCase(
                    DeleteUserGroupUseCase.Params(userId, groupId)
                )
            }.onSuccess {
                Log.e("GroupDetailsViewModel", "User groups deleted successfully")
            }.onFailure { e ->
                Log.e("GroupDetailsViewModel", "Error deleting user groups: ${e.message}")
            }
        }
    }
}