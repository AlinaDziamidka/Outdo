package com.example.graduationproject.presentation.notifications

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.usecase.DeleteNotificationUseCase
import com.example.graduationproject.domain.usecase.DeleteUserGroupUseCase
import com.example.graduationproject.domain.usecase.FetchNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    context: Application,
    private val fetchNotificationsUseCase: FetchNotificationsUseCase,
    private val deleteUserGroupUseCase: DeleteUserGroupUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase
) : AndroidViewModel(context) {

    private val _viewState =
        MutableStateFlow<NotificationsViewState<MutableList<Pair<UserProfile, Group>>>>(
            NotificationsViewState.Loading
        )
    val viewState: StateFlow<NotificationsViewState<MutableList<Pair<UserProfile, Group>>>> =
        _viewState

    fun setNotifications(userId: String) {
        viewModelScope.launch {
            fetchNotificationsUseCase(
                FetchNotificationsUseCase.Params(userId)
            )
                .onStart {
                    _viewState.value = NotificationsViewState.Loading
                }.catch {
                    _viewState.value =
                        NotificationsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { notifications ->
                    _viewState.value = NotificationsViewState.Success(notifications)
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
                Log.e("NotificationViewModel", "User groups deleted successfully")
            }.onFailure { e ->
                Log.e("NotificationViewModel", "Error deleting user groups: ${e.message}")
            }
        }
    }

    fun deleteNotification(userId: String, groupId: String) {
        viewModelScope.launch {
            runCatching {
                deleteNotificationUseCase(
                    DeleteNotificationUseCase.Params(userId, groupId)
                )
            }.onSuccess {
                Log.e("NotificationViewModel", "Notification deleted successfully")
            }.onFailure { e ->
                Log.e("NotificationViewModel", "Error deleting notification: ${e.message}")
            }
        }
    }
}