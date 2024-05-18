package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.service.AchievementApiService
import com.example.graduationproject.data.remote.transormer.AchievementTransformer
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.repository.remote.AchievementRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall

class AchievementRemoteRepositoryImpl(private val achievementApiService: AchievementApiService) :
    AchievementRemoteRepository {
    override suspend fun fetchAchievementById(achievementIdQuery: String): Event<Achievement> {
        val idQuery = "objectId=\'$achievementIdQuery\'"
        Log.d("AchievementRepositoryImpl", "Fetching achievement with query: $idQuery")
        val event = doCall {
            return@doCall achievementApiService.fetchAchievementById(idQuery)
        }

        return when (event) {
            is Event.Success -> {
                Log.d("AchievementRepositoryImpl", "Received response: ${event.data}")
                val response = event.data.first()
                val achievementTransformer = AchievementTransformer()
                val achievement = achievementTransformer.fromResponse(response)
                Log.d(
                    "AchievementRepositoryImpl",
                    "Transformed response to challenge: $achievement"
                )
                Event.Success(achievement)
            }

            is Event.Failure -> {
                Log.e("AchievementRepositoryImpl", "Failed to fetch challenges: ${event.exception}")
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
                val achievementTransformer = AchievementTransformer()
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