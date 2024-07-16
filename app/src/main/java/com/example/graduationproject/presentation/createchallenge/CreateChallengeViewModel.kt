package com.example.graduationproject.presentation.createchallenge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.usecase.CreateGroupUseCase
import com.example.graduationproject.domain.usecase.NotificationUseCase
import com.example.graduationproject.domain.usecase.SetGroupParticipantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CreateChallengeViewModel @Inject constructor(
    context: Application,
    private val createGroupUseCase: CreateGroupUseCase,
    private val setGroupParticipantsUseCase: SetGroupParticipantsUseCase,
    private val notificationUseCase: NotificationUseCase
) : AndroidViewModel(context) {

    private val _addedFriends = MutableStateFlow<MutableList<UserProfile>>(mutableListOf())
    val addedFriends: StateFlow<MutableList<UserProfile>> get() = _addedFriends

    fun deleteFriend(friend: UserProfile) {
        _addedFriends.value.remove(friend)
    }

    fun addFriends(newFriends: List<UserProfile>) {
        _addedFriends.value.addAll(newFriends)
    }

//    fun deleteFriend(friend: UserProfile) {
//        val updatedFriends = _addedFriends.value.toMutableList().apply {
//            remove(friend)
//        }
//        _addedFriends.value = updatedFriends
//    }
//
//    fun addFriends(newFriends: List<UserProfile>) {
//        val updatedFriends = _addedFriends.value.toMutableList().apply {
//            addAll(newFriends)
//        }
//        _addedFriends.value = updatedFriends
//    }



    private val _achievementCards =
        MutableStateFlow<MutableList<Pair<String, String>>>(mutableListOf())
    val achievementCards: StateFlow<MutableList<Pair<String, String>>> get() = _achievementCards

//    fun addAchievementsCard(achievements: MutableList<Pair<String, String>>) {
//        _achievementCards.value.clear()
//        _achievementCards.value.addAll(achievements)
//    }

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



}
