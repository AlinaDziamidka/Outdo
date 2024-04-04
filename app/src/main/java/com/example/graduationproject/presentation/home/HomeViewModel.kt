package com.example.graduationproject.presentation.home

import android.app.Application
import android.util.Log

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.usecase.FetchUserGroupChallengesUseCase
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
    private val fetchUserGroupChallengesUseCase: FetchUserGroupChallengesUseCase
) :
    AndroidViewModel(context) {

    private val _viewState =
        MutableStateFlow<HomeViewState<List<GroupAndChallenges>>>(HomeViewState.Loading)
    val viewState: StateFlow<HomeViewState<List<GroupAndChallenges>>> = _viewState
    fun setUpUserGroups(userId: String) {
        viewModelScope.launch {
            Log.d("HomeViewModel", "setUpUserGroups: starting request for userId: $userId")
            fetchUserGroupChallengesUseCase(FetchUserGroupChallengesUseCase.Params(userId))
                .onStart {
                    Log.d("HomeViewModel", "setUpUserGroups: fetching data started")
                    _viewState.value = HomeViewState.Loading
                }.catch {
                    Log.e("HomeViewModel", "setUpUserGroups: error occurred: ${it.message}")
                    _viewState.value =
                        HomeViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { groupAndChallenges ->
                    Log.d("HomeViewModel", "setUpUserGroups: data collected")
                        _viewState.value = HomeViewState.Success(groupAndChallenges)
                    }
                }
        }
}