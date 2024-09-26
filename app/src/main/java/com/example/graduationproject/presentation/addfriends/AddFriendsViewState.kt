package com.example.graduationproject.presentation.addfriends

interface AddFriendsViewState <out T>{

data class Success<T> (val data: T) : AddFriendsViewState<T>
data class Failure(val message: String) : AddFriendsViewState<Nothing>
data object Loading : AddFriendsViewState<Nothing>
}