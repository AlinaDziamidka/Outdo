package com.example.graduationproject.presentation.addfriends

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.usecase.FetchFriendsUseCase
import com.example.graduationproject.domain.usecase.FetchGroupParticipantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendsViewModel @Inject constructor(
    context: Application,
    private val fetchFriendsUseCase: FetchFriendsUseCase,
    private val fetchGroupParticipantsUseCase: FetchGroupParticipantsUseCase
) :
    AndroidViewModel(context) {

    private val _viewState =
        MutableStateFlow<AddFriendsViewState<MutableList<UserProfile>>>(AddFriendsViewState.Loading)
    val viewState: StateFlow<AddFriendsViewState<MutableList<UserProfile>>> =
        _viewState

    private val _selectedFriends = MutableStateFlow<MutableList<UserProfile>>(mutableListOf())
    val selectedFriends: StateFlow<MutableList<UserProfile>> get() = _selectedFriends

    fun addFriends(newFriends: List<UserProfile>) {
        _selectedFriends.value += newFriends
    }

    fun setUpFriends(userId: String) {
        viewModelScope.launch {
            fetchFriendsUseCase(
                FetchFriendsUseCase.Params(
                    userId
                )
            )
                .onStart {
                    _viewState.value = AddFriendsViewState.Loading
                }.catch {
                    _viewState.value =
                        AddFriendsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { friends ->
                    _viewState.value = AddFriendsViewState.Success(friends)
                }
        }
    }

    fun setUpGroupFriends(groupId: String) {
        viewModelScope.launch {
            fetchGroupParticipantsUseCase(
                FetchGroupParticipantsUseCase.Params(
                    groupId
                )
            )
                .onStart {
                    _viewState.value = AddFriendsViewState.Loading
                }.catch {
                    _viewState.value =
                        AddFriendsViewState.Failure(it.message ?: "Something went wrong.")
                }.collect { friends ->
                    _viewState.value = AddFriendsViewState.Success(friends)
                }
        }
    }
}