package com.example.graduationproject.presentation.challengedetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.usecase.FetchChallengeAchievementUseCase
import com.example.graduationproject.domain.usecase.FetchChallengeDescriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeDetailsViewModel @Inject constructor(
    context: Application,
    private val fetchChallengeAchievementUseCase: FetchChallengeAchievementUseCase,
    private val fetchChallengeDescriptionUseCase: FetchChallengeDescriptionUseCase
) :
    AndroidViewModel(context) {

    private val _viewStateAchievements =
        MutableStateFlow<ChallengeDetailsViewState<MutableList<Achievement>>>(
            ChallengeDetailsViewState.Loading
        )
    val viewStateAchievements: StateFlow<ChallengeDetailsViewState<MutableList<Achievement>>> =
        _viewStateAchievements

    private val _viewStateCurrentChallenge =
        MutableStateFlow<ChallengeDetailsViewState<Pair<Challenge, UserProfile?>>>(
            ChallengeDetailsViewState.Loading
        )
    val viewStateCurrentChallenge: StateFlow<ChallengeDetailsViewState<Pair<Challenge, UserProfile?>>> =
        _viewStateCurrentChallenge

    fun setCurrentChallenge(challengeId: String) {
        viewModelScope.launch {
            fetchChallengeDescriptionUseCase(
                FetchChallengeDescriptionUseCase.Params(challengeId)
            )
                .onStart {
                    _viewStateCurrentChallenge.value = ChallengeDetailsViewState.Loading
                }.catch {
                    _viewStateCurrentChallenge.value =
                        ChallengeDetailsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect {
                    _viewStateCurrentChallenge.value = ChallengeDetailsViewState.Success(it)
                }
        }
    }

    fun setUpAchievements(challengeId: String) {
        viewModelScope.launch {
            fetchChallengeAchievementUseCase(
                FetchChallengeAchievementUseCase.Params(
                    challengeId
                )
            )
                .onStart {
                    _viewStateAchievements.value = ChallengeDetailsViewState.Loading
                }.catch {
                    _viewStateAchievements.value =
                        ChallengeDetailsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { achievements ->
                    _viewStateAchievements.value = ChallengeDetailsViewState.Success(achievements)
                }
        }
    }
}