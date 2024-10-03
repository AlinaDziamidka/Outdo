package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.request.UpdatePhotoRequest
import com.example.graduationproject.data.remote.api.request.UserAchievementRequest
import com.example.graduationproject.data.remote.api.service.UserAchievementApiService
import com.example.graduationproject.data.remote.transormer.UserAchievementTransformer
import com.example.graduationproject.domain.entity.AchievementStatus
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.entity.UserAchievement
import com.example.graduationproject.domain.repository.remote.UserAchievementRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class UserAchievementRemoteRepositoryImpl @Inject constructor(
    private val userAchievementApiService: UserAchievementApiService
) :
    UserAchievementRemoteRepository {

    private val userAchievementTransformer = UserAchievementTransformer()

    override suspend fun fetchUserAchievementByUserId(userIdQuery: String): Event<List<UserAchievement>> {
        val query = "userId=\'$userIdQuery\'"
        val event = doCall {
            return@doCall userAchievementApiService.fetchAchievementsByUserId(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val userAchievement = response.map { userAchievementResponse ->
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

    override suspend fun fetchByUserIdAndAchievementId(
        achievementIdQuery: String,
        userIdQuery: String
    ): Event<UserAchievement> {

        val query = "userId='$userIdQuery' AND achievementId='$achievementIdQuery'"

        val event = doCall {
            return@doCall userAchievementApiService.fetchByUserIdAndAchievementId(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data.first()
                val userAchievement = userAchievementTransformer.fromResponse(response)
                Event.Success(userAchievement)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun insertUserAchievements(
        userId: String,
        achievementId: String
    ): Event<UserAchievement> {
        val event = doCall {
            val request = UserAchievementRequest(
                userId = userId,
                achievementId = achievementId,
                achievementStatus = AchievementStatus.UNCOMPLETED.stringValue,
                achievementType = AchievementType.CHALLENGE.stringValue
            )
            return@doCall userAchievementApiService.insertUserAchievements(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val userAchievement = userAchievementTransformer.fromResponse(response)
                Event.Success(userAchievement)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun updatePhoto(
        userIdQuery: String,
        achievementIdQuery: String,
        photoUrl: String
    ): Event<Long> {

        val query = "userId='$userIdQuery' AND achievementId='$achievementIdQuery'"

        val event = doCall {
            val request = UpdatePhotoRequest(photoUrl, AchievementStatus.COMPLETED.stringValue)
            return@doCall userAchievementApiService.updatePhoto(
                query,
                request
            )
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                Event.Success(response)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}
