package com.example.graduationproject.presentation.challenges

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.usecase.local.FetchLocalUserGroupChallengesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengesViewModel @Inject constructor(
    context: Application,
    private val fetchLocalUserGroupChallengesUseCase: FetchLocalUserGroupChallengesUseCase
) : AndroidViewModel(context) {

    private val _viewState =
        MutableStateFlow<ChallengeViewState<List<GroupAndChallenges>>>(ChallengeViewState.Loading)
    val viewState: StateFlow<ChallengeViewState<List<GroupAndChallenges>>> = _viewState

    fun setUpUserChallenges(userId: String) {
        viewModelScope.launch {
            fetchLocalUserGroupChallengesUseCase(
                FetchLocalUserGroupChallengesUseCase.Params(
                    userId
                )
            )
                .onStart {
                    _viewState.value = ChallengeViewState.Loading
                }.catch {
                    _viewState.value =
                        ChallengeViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { groupAndChallenges ->
                    _viewState.value = ChallengeViewState.Success(groupAndChallenges)
                }
        }
    }
}

