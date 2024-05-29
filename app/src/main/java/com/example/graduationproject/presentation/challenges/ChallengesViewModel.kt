package com.example.graduationproject.presentation.challenges

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.usecase.FetchUserGroupChallengesUseCase
import com.example.graduationproject.domain.usecase.FetchUserGroupsUseCase
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
    private val fetchUserGroupChallengesUseCase: FetchUserGroupChallengesUseCase
) : AndroidViewModel(context) {

    private val _viewState =
        MutableStateFlow<ChallengeViewState<List<GroupAndChallenges>>>(ChallengeViewState.Loading)
    val viewState: StateFlow<ChallengeViewState<List<GroupAndChallenges>>> = _viewState

    fun setUpUserChallenges(userId: String) {
        viewModelScope.launch {
            fetchUserGroupChallengesUseCase(
                FetchUserGroupChallengesUseCase.Params(
                    userId, ChallengeStatus.UNFINISHED
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

