package com.example.graduationproject.presentation.creategroup

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.usecase.CreateGroupUseCase
import com.example.graduationproject.domain.usecase.NotificationUseCase
import com.example.graduationproject.domain.usecase.SetGroupParticipantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    context: Application,
    private val createGroupUseCase: CreateGroupUseCase,
    private val setGroupParticipantsUseCase: SetGroupParticipantsUseCase,
    private val notificationUseCase: NotificationUseCase
) : AndroidViewModel(context) {

    private val _viewState =
        MutableStateFlow<CreateGroupViewState<Group>>(CreateGroupViewState.Loading)
    val viewState: StateFlow<CreateGroupViewState<Group>> =
        _viewState

    private val _addedFriends = MutableStateFlow<MutableList<UserProfile>>(mutableListOf())
    val addedFriends: StateFlow<MutableList<UserProfile>> get() = _addedFriends

    private val _groupName = MutableStateFlow("")
    val groupName: StateFlow<String> = _groupName

    fun setGroupName(name: String) {
        _groupName.value = name
    }

    fun deleteFriend(friend: UserProfile) {
        _addedFriends.value.remove(friend)
    }

    fun addFriends(newFriends: List<UserProfile>) {
        _addedFriends.value.addAll(newFriends)
    }

    fun setGroup(groupName: String, creatorId: String, groupAvatarPath: String?) {
        viewModelScope.launch {
            createGroupUseCase(
                CreateGroupUseCase.Params(groupName, creatorId, groupAvatarPath)
            )
                .onStart {
                    _viewState.value = CreateGroupViewState.Loading
                }.catch {
                    _viewState.value =
                        CreateGroupViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { group ->
                    _viewState.value = CreateGroupViewState.Success(group)
                }
        }
    }

    fun addGroupParticipants(groupId: String, friends: List<UserProfile>) {
        viewModelScope.launch {
            runCatching {
                setGroupParticipantsUseCase(SetGroupParticipantsUseCase.Params(groupId, friends))
            }.onSuccess {
                Log.e("CreateGroupViewModel", "User groups added successfully")
            }.onFailure { e ->
                Log.e("CreateGroupViewModel", "Error adding user groups: ${e.message}")
            }
        }
    }

    fun notifyParticipants(friends: MutableList<UserProfile>, message: String, group: Group, creatorId: String) {
        viewModelScope.launch {
            runCatching {
                notificationUseCase(NotificationUseCase.Params(friends, message, group, creatorId))
            }.onSuccess {
                Log.e("CreateGroupViewModel", "Notification sent successfully")
            }.onFailure { e ->
                Log.e("CreateGroupViewModel", "Error sending notification: ${e.message}")
            }
        }
    }
}