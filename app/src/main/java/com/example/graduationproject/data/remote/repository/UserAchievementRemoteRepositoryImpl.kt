package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.response.AchievementResponse
import com.example.graduationproject.data.remote.api.service.AchievementApiService
import com.example.graduationproject.data.remote.api.service.UserAchievementApiService
import com.example.graduationproject.data.remote.transormer.AchievementTransformer
import com.example.graduationproject.data.remote.transormer.UserAchievementTransformer
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.UserAchievement
import com.example.graduationproject.domain.repository.remote.UserAchievementRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class UserAchievementRemoteRepositoryImpl @Inject constructor(
    private val userAchievementApiService: UserAchievementApiService
) :
    UserAchievementRemoteRepository {
    override suspend fun fetchUserAchievementByUserId(userIdQuery: String): Event<List<UserAchievement>> {
        val query = "userId=\'$userIdQuery\'"
        val event = doCall {
            return@doCall userAchievementApiService.fetchAchievementsByUserId(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val userAchievement = response.map { userAchievementResponse ->
                    val userAchievementTransformer = UserAchievementTransformer()
                    userAchievementTransformer.fromResponse(userAchievementResponse)
                }
                Event.Success(userAchievement)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun fetchUsersByAchievementId(achievementIdQuery: String): Event<List<UserAchievement>> {
        val query = "achievementId=\'$achievementIdQuery\'"
        val event = doCall {
            return@doCall userAchievementApiService.fetchUsersByAchievementId(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val achievementUser = response.map { userAchievementResponse ->
                    val userAchievementTransformer = UserAchievementTransformer()
                    userAchievementTransformer.fromResponse(userAchievementResponse)
                }
                Event.Success(achievementUser)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}