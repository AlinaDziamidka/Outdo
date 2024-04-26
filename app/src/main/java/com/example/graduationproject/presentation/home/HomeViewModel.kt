package com.example.graduationproject.presentation.home

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.usecase.local.FetchLocalUserGroupChallengesUseCase
import com.example.graduationproject.domain.usecase.local.FetchLocalWeekChallengeUseCase
import com.example.graduationproject.domain.usecase.remote.FetchRemoteUserGroupChallengesUseCase
import com.example.graduationproject.domain.usecase.remote.FetchRemoteWeekChallengeUseCase
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
    private val fetchRemoteUserGroupChallengesUseCase: FetchRemoteUserGroupChallengesUseCase,
    private val fetchLocalUserGroupChallengesUseCase: FetchLocalUserGroupChallengesUseCase,
    private val fetchRemoteWeekChallengeUseCase: FetchRemoteWeekChallengeUseCase,
    private val fetchLocalWeekChallengeUseCase: FetchLocalWeekChallengeUseCase
) :
    AndroidViewModel(context) {

    private val _viewStateChallenges =
        MutableStateFlow<HomeViewState<List<GroupAndChallenges>>>(HomeViewState.Loading)
    val viewStateChallenges: StateFlow<HomeViewState<List<GroupAndChallenges>>> =
        _viewStateChallenges

    private val _viewStateWeek =
        MutableStateFlow<HomeViewState<Challenge>>(HomeViewState.Loading)
    val viewStateWeek: StateFlow<HomeViewState<Challenge>> = _viewStateWeek

    private var isDatabaseLoaded = false

    fun setDatabaseLoadedStatus(loaded: Boolean) {
        isDatabaseLoaded = loaded
    }

    fun setUpUserChallenges(userId: String) {
        if (isDatabaseLoaded) {
            fetchLocalGroupChallenges(userId)
        } else {
            fetchRemoteGroupChallenges(userId)
        }
    }

    private fun fetchRemoteGroupChallenges(userId: String) {
        viewModelScope.launch {
            fetchRemoteUserGroupChallengesUseCase(
                FetchRemoteUserGroupChallengesUseCase.Params(
                    userId
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

    private fun fetchLocalGroupChallenges(userId: String) {
        viewModelScope.launch {
            fetchLocalUserGroupChallengesUseCase(
                FetchLocalUserGroupChallengesUseCase.Params(
                    userId
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
            fetchRemoteWeekChallengeUseCase(
                FetchRemoteWeekChallengeUseCase.Params(challengeType)
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

}