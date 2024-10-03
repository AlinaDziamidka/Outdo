package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.entity.AchievementStatus
import com.example.graduationproject.domain.entity.UserAchievement
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.local.UserAchievementLocalRepository
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.repository.remote.UserAchievementRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchCompletedFriendsUseCase @Inject constructor(
    private val userAchievementRemoteRepository: UserAchievementRemoteRepository,
    private val userAchievementLocalRepository: UserAchievementLocalRepository,
    private val userRemoteRepository: UserRemoteRepository,
    private val userLocalRepository: UserLocalRepository
) : UseCase<FetchCompletedFriendsUseCase.Params, MutableList<UserProfile>> {

    data class Params(
        val achievementId: String
    )

    override suspend fun invoke(params: Params): Flow<MutableList<UserProfile>> =
        flow {
            val achievementId = params.achievementId
            val users = mutableListOf<UserProfile>()
            val userAchievements =
                fetchUserAchievements(achievementId)

            val completedUserAchievements =
                userAchievements.filter { it.achievementStatus == AchievementStatus.COMPLETED }

            fetchUsers(completedUserAchievements, users)
            emit(users)
        }

    private suspend fun fetchUsers(
        completedUserAchievements: List<UserAchievement>,
        users: MutableList<UserProfile>
    ) {
        if (completedUserAchievements.isNotEmpty()) {
            completedUserAchievements.forEach { userAchievement ->
                withContext(Dispatchers.IO) {
                    val localUser = userLocalRepository.fetchById(userAchievement.userId)
                    users.add(localUser)
                }
            }

            if (users.isEmpty()) {
                completedUserAchievements.forEach { userAchievement ->
                    val userEvent = userRemoteRepository.fetchUserById(userAchievement.userId)
                    if (userEvent is Event.Success) {
                        val remoteUser = userEvent.data
                        users.add(remoteUser)
                    }
                }
            }
        }
    }

    private suspend fun fetchUserAchievements(achievementId: String): List<UserAchievement> {
        var userAchievements =
            withContext(Dispatchers.IO) {
                userAchievementLocalRepository.fetchUsersByAchievementId(achievementId)
            }
        if (userAchievements.isEmpty()) {
            val userAchievementsEvent =
                userAchievementRemoteRepository.fetchUsersByAchievementId(achievementId)
            if (userAchievementsEvent is Event.Success) {
                userAchievements = userAchievementsEvent.data
            }
        }
        return userAchievements
    }
}