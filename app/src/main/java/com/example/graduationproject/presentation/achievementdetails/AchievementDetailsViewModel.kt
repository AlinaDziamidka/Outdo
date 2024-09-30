package com.example.graduationproject.presentation.achievementdetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.usecase.FetchAchievementDescriptionUseCase
import com.example.graduationproject.domain.usecase.FetchCompletedFriendsUseCase
import com.example.graduationproject.domain.usecase.FetchFriendPhotoUseCase
import com.example.graduationproject.domain.usecase.FetchUncompletedFriendsUseCase
import com.example.graduationproject.domain.usecase.PhotoUploadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AchievementDetailsViewModel @Inject constructor(
    context: Application,
    private val fetchAchievementDescriptionUseCase: FetchAchievementDescriptionUseCase,
    private val fetchCompletedFriendsUseCase: FetchCompletedFriendsUseCase,
    private val fetchUncompletedFriendsUseCase: FetchUncompletedFriendsUseCase,
    private val photoUploadUseCase: PhotoUploadUseCase,
    private val fetchFriendPhotoUseCase: FetchFriendPhotoUseCase
) : AndroidViewModel(context) {

    private val _viewStateCurrentAchievement =
        MutableStateFlow<AchievementDetailsViewState<Achievement>>(
            AchievementDetailsViewState.Loading
        )
    val viewStateCurrentAchievement: StateFlow<AchievementDetailsViewState<Achievement>> =
        _viewStateCurrentAchievement

    private val _viewStateUpload = MutableStateFlow<AchievementDetailsViewState<String>>(
        AchievementDetailsViewState.Loading
    )
    val viewStateUpload: StateFlow<AchievementDetailsViewState<String>> = _viewStateUpload

    private val _viewStateDownload = MutableStateFlow<AchievementDetailsViewState<String>>(
        AchievementDetailsViewState.Loading
    )
    val viewStateDownload: StateFlow<AchievementDetailsViewState<String>> = _viewStateDownload

    private val _viewStateCompletedFriends =
        MutableStateFlow<AchievementDetailsViewState<MutableList<UserProfile>>>(
            AchievementDetailsViewState.Loading
        )
    val viewStateCompletedFriends: StateFlow<AchievementDetailsViewState<MutableList<UserProfile>>> =
        _viewStateCompletedFriends

    private val _viewStateUncompletedFriends =
        MutableStateFlow<AchievementDetailsViewState<MutableList<UserProfile>>>(
            AchievementDetailsViewState.Loading
        )
    val viewStateUncompletedFriends: StateFlow<AchievementDetailsViewState<MutableList<UserProfile>>> =
        _viewStateUncompletedFriends


    fun setCurrentAchievement(achievementId: String) {
        viewModelScope.launch {
            fetchAchievementDescriptionUseCase(
                FetchAchievementDescriptionUseCase.Params(achievementId)
            ).onStart {
                _viewStateCurrentAchievement.value = AchievementDetailsViewState.Loading
            }.catch {
                _viewStateCurrentAchievement.value =
                    AchievementDetailsViewState.Failure(it.message ?: "Something went wrong.")
            }.collect {
                _viewStateCurrentAchievement.value = AchievementDetailsViewState.Success(it)
            }
        }
    }

    fun setUpCompletedFriends(achievementId: String) {
        viewModelScope.launch {
            fetchCompletedFriendsUseCase(
                FetchCompletedFriendsUseCase.Params(achievementId)
            ).onStart {
                _viewStateCompletedFriends.value = AchievementDetailsViewState.Loading
            }.catch {
                _viewStateCompletedFriends.value =
                    AchievementDetailsViewState.Failure(it.message ?: "Something went wrong.")
            }.collect {
                _viewStateCompletedFriends.value = AchievementDetailsViewState.Success(it)
            }
        }
    }

    fun setUpUncompletedFriends(achievementId: String) {
        viewModelScope.launch {
            fetchUncompletedFriendsUseCase(
                FetchUncompletedFriendsUseCase.Params(achievementId)
            ).onStart {
                _viewStateUncompletedFriends.value = AchievementDetailsViewState.Loading
            }.catch {
                _viewStateUncompletedFriends.value =
                    AchievementDetailsViewState.Failure(it.message ?: "Something went wrong.")
            }.collect {
                _viewStateUncompletedFriends.value = AchievementDetailsViewState.Success(it)
            }
        }
    }

    fun setUpPhoto(userId: String, achievementId: String, photo: File) {
        viewModelScope.launch {
            photoUploadUseCase(
                PhotoUploadUseCase.Params(userId, achievementId, photo)
            ).onStart {
                _viewStateUpload.value = AchievementDetailsViewState.Loading
            }.catch {
                _viewStateUpload.value =
                    AchievementDetailsViewState.Failure(it.message ?: "Something went wrong.")
            }.collect {
                _viewStateUpload.value = AchievementDetailsViewState.Success(it)
            }
        }
    }

    fun getPhoto(userId: String, achievementId: String) {
        viewModelScope.launch {
            fetchFriendPhotoUseCase(
                FetchFriendPhotoUseCase.Params(userId, achievementId)
            ).onStart {
                _viewStateDownload.value = AchievementDetailsViewState.Loading
            }.catch {
                _viewStateDownload.value =
                    AchievementDetailsViewState.Failure(it.message ?: "Something went wrong.")
            }.collect {
                _viewStateDownload.value = AchievementDetailsViewState.Success(it)
            }
        }
    }
}