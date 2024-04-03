package com.example.graduationproject.presentation.home

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.usecase.FetchUserGroupChallengesUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    context: Application,
    private val fetchUserGroupChallengesUseCase: FetchUserGroupChallengesUseCase
) :
    AndroidViewModel(context) {

    private val _viewState = MutableStateFlow<HomeViewState>(HomeViewState.Idle)
    val viewState = _viewState.asStateFlow()

    private val _challenges = MutableStateFlow<List<GroupAndChallenges>>(emptyList())
    val challenges: StateFlow<List<GroupAndChallenges>> get() = _challenges

    fun setUpUserGroups(userId: String) {
        viewModelScope.launch {
            fetchUserGroupChallengesUseCase(FetchUserGroupChallengesUseCase.Params(userId))
                .onStart {
                    _viewState.value = HomeViewState.Loading
                }.catch {
                    _viewState.value =
                        HomeViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { challenges ->
                    _viewState.value = HomeViewState.Success
                    _challenges.value = challenges
                }
        }
    }
}