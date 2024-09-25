package com.example.graduationproject.presentation.home

import android.app.Application
import android.util.Log

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.usecase.FetchDailyAchievementUseCase
import com.example.graduationproject.domain.usecase.FetchNotificationsUseCase
import com.example.graduationproject.domain.usecase.FetchUserGroupChallengesUseCase
import com.example.graduationproject.domain.usecase.FetchWeekChallengeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    context: Application,
    private val fetchUserGroupChallengesUseCase: FetchUserGroupChallengesUseCase,
    private val fetchWeekChallengeUseCase: FetchWeekChallengeUseCase,
    private val fetchDailyAchievementUseCase: FetchDailyAchievementUseCase,
    private val fetchNotificationsUseCase: FetchNotificationsUseCase
) :
    AndroidViewModel(context) {

    private val _viewStateChallenges =
        MutableStateFlow<HomeViewState<List<GroupAndChallenges>>>(HomeViewState.Loading)
    val viewStateChallenges: StateFlow<HomeViewState<List<GroupAndChallenges>>> =
        _viewStateChallenges

    private val _viewStateWeek =
        MutableStateFlow<HomeViewState<Challenge>>(HomeViewState.Loading)
    val viewStateWeek: StateFlow<HomeViewState<Challenge>> = _viewStateWeek

    private val _viewStateDaily =
        MutableStateFlow<HomeViewState<Achievement>>(HomeViewState.Loading)
    val viewStateDaily: StateFlow<HomeViewState<Achievement>> = _viewStateDaily

    private val _viewStateNotification =
        MutableStateFlow<HomeViewState<MutableList<Pair<UserProfile, Group>>>>(
            HomeViewState.Loading
        )
    val viewStateNotification: StateFlow<HomeViewState<MutableList<Pair<UserProfile, Group>>>> =
        _viewStateNotification

    fun setUpUserChallenges(userId: String) {
        viewModelScope.launch {
            fetchUserGroupChallengesUseCase(
                FetchUserGroupChallengesUseCase.Params(
                    userId, ChallengeStatus.UNFINISHED
                )
            )
                .onStart {
                    _viewStateChallenges.value = HomeViewState.Loading
                }.catch {
                    _viewStateChallenges.value =
                        HomeViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { groupAndChallenges ->
                    _viewStateChallenges.value = HomeViewState.Success(groupAndChallenges)
                }
        }
    }

    fun setUpWeekChallenge(challengeType: ChallengeType) {
        viewModelScope.launch {
            fetchWeekChallengeUseCase(
                FetchWeekChallengeUseCase.Params(challengeType)
            )
                .onStart {
                    _viewStateWeek.value = HomeViewState.Loading
                }.catch {
                    _viewStateWeek.value =
                        HomeViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { challenge ->
                    _viewStateWeek.value = HomeViewState.Success(challenge)
                }
        }
    }

    fun setUpDailyAchievement(achievementType: AchievementType) {
        viewModelScope.launch {
            fetchDailyAchievementUseCase(
                FetchDailyAchievementUseCase.Params(achievementType)
            )
                .onStart {
                    _viewStateDaily.value = HomeViewState.Loading
                }.catch {
                    _viewStateDaily.value =
                        HomeViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { achievement ->
                    _viewStateDaily.value = HomeViewState.Success(achievement)
                }
        }
    }

    fun setNotifications(userId: String) {
        viewModelScope.launch {
            fetchNotificationsUseCase(
                FetchNotificationsUseCase.Params(userId)
            )
                .onStart {
                    _viewStateNotification.value = HomeViewState.Loading
                }.catch {
                    _viewStateNotification.value =
                        HomeViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { notifications ->
                    _viewStateNotification.value = HomeViewState.Success(notifications)
                }
        }
    }
}