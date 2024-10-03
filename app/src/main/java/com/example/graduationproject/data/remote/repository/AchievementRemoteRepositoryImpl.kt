package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.request.AchievementRequest
import com.example.graduationproject.data.remote.api.service.AchievementApiService
import com.example.graduationproject.data.remote.transormer.AchievementTransformer
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementStatus
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.repository.remote.AchievementRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class AchievementRemoteRepositoryImpl @Inject constructor(private val achievementApiService: AchievementApiService) :
    AchievementRemoteRepository {

    private val achievementTransformer = AchievementTransformer()

    override suspend fun fetchAchievementById(achievementIdQuery: String): Event<Achievement> {
        val idQuery = "objectId=\'$achievementIdQuery\'"
        val event = doCall {
            return@doCall achievementApiService.fetchAchievementById(idQuery)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data.first()
                val achievement = achievementTransformer.fromResponse(response)
                Event.Success(achievement)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun fetchDailyAchievement(achievementTypeQuery: String): Event<Achievement> {
        val typeQuery = "achievementType = \'$achievementTypeQuery\'"
        val event = doCall {
            return@doCall achievementApiService.fetchDailyAchievement(typeQuery)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data.first()
                val achievement = achievementTransformer.fromResponse(response)
                Event.Success(achievement)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun insertAchievement(
        achievementName: String,
        description: String,
        endTime: Long
    ): Event<Achievement> {
        val event = doCall {
            val request = AchievementRequest(
                achievementName = achievementName,
                description = description,
                categoryId = null,
                achievementType = AchievementType.CHALLENGE.stringValue,
                achievementStatus = AchievementStatus.UNCOMPLETED.stringValue,
                achievementIcon = null,
                endTime = endTime
            )
            return@doCall achievementApiService.insertAchievement(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val achievement = achievementTransformer.fromResponse(response)
                Event.Success(achievement)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}