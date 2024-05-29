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
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.usecase.FetchDailyAchievementUseCase
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
    private val fetchDailyAchievementUseCase: FetchDailyAchievementUseCase
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

    fun setUpUserChallenges(userId: String) {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Fetching remote group challenges for user $userId")
            fetchUserGroupChallengesUseCase(
                FetchUserGroupChallengesUseCase.Params(
                    userId, ChallengeStatus.UNFINISHED
                )
            )
                .onStart {
                    Log.d("HomeViewModel", "Fetching remote group challenges: Loading state")
                    _viewStateChallenges.value = HomeViewState.Loading
                }.catch {
                    Log.e(
                        "HomeViewModel",
                        "Fetching remote group challenges: Error - ${it.message}"
                    )
                    _viewStateChallenges.value =
                        HomeViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { groupAndChallenges ->
                    Log.d("HomeViewModel", "Fetching remote group challenges: Success")
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
}