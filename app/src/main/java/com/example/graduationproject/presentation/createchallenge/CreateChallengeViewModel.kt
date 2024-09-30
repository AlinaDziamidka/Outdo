package com.example.graduationproject.presentation.createchallenge

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.usecase.CreateChallengeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateChallengeViewModel @Inject constructor(
    context: Application,
    private val createChallengeUseCase: CreateChallengeUseCase
) : AndroidViewModel(context) {

    private val _viewState =
        MutableStateFlow<CreateChallengeViewState<Challenge>>(CreateChallengeViewState.Loading)
    val viewState: StateFlow<CreateChallengeViewState<Challenge>> =
        _viewState

    private val _addedFriends = MutableStateFlow<MutableList<UserProfile>>(mutableListOf())
    val addedFriends: StateFlow<MutableList<UserProfile>> get() = _addedFriends

    fun deleteFriend(friend: UserProfile) {
        _addedFriends.value.remove(friend)
    }

    fun addFriends(newFriends: List<UserProfile>) {
        _addedFriends.value.addAll(newFriends)
    }

    private val _achievementCards =
        MutableStateFlow<MutableList<Pair<String, String>>>(mutableListOf())
    val achievementCards: StateFlow<MutableList<Pair<String, String>>> get() = _achievementCards

    fun addAchievementCard(achievement: Pair<String, String>) {
        _achievementCards.value = _achievementCards.value.toMutableList().apply { add(achievement) }
    }

    fun updateAchievementCard(index: Int, achievement: Pair<String, String>) {
        _achievementCards.value = _achievementCards.value.toMutableList().apply {
            if (index in indices) {
                set(index, achievement)
            }
        }
    }

    fun setChallenge(
        groupId: String,
        challengeName: String,
        creatorId: String,
        startDate: Long,
        finishDate: Long,
        description: String,
        achievements: List<Pair<String, String>>,
        friends: List<UserProfile>
    ) {
        viewModelScope.launch {
            createChallengeUseCase(
                CreateChallengeUseCase.Params(
                    groupId,
                    challengeName,
                    creatorId,
                    startDate,
                    finishDate,
                    description,
                    achievements,
                    friends
                )
            )
                .onStart {
                    _viewState.value = CreateChallengeViewState.Loading
                }.catch {
                    _viewState.value =
                        CreateChallengeViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { challenge ->
                    _viewState.value = CreateChallengeViewState.Success(challenge)
                }
        }
    }
}
