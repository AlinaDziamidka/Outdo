package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.request.ChallengeAchievementRequest
import com.example.graduationproject.data.remote.api.response.AchievementResponse
import com.example.graduationproject.data.remote.api.service.AchievementApiService
import com.example.graduationproject.data.remote.api.service.ChallengeAchievementApiService
import com.example.graduationproject.data.remote.transormer.AchievementTransformer
import com.example.graduationproject.data.remote.transormer.ChallengeAchievementTransformer
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.ChallengeAchievement
import com.example.graduationproject.domain.repository.remote.ChallengeAchievementRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class ChallengeAchievementRemoteRepositoryImpl @Inject constructor(
    private val challengeAchievementApiService: ChallengeAchievementApiService,
    private val achievementApiService: AchievementApiService
) : ChallengeAchievementRemoteRepository {

    override suspend fun fetchAllAchievementsByChallengeId(challengeIdQuery: String): Event<List<Achievement>> {
        val query = "challengeId=\'$challengeIdQuery\'"
        val event = doCall {
            return@doCall challengeAchievementApiService.fetchAllAchievementsByChallengeId(query)
        }

        return when (event) {
            is Event.Success -> {
                val achievements = mutableListOf<Achievement>()
                val response = event.data

                response.forEach { challengeAchievementResponse ->
                    val achievementEvent =
                        getAchievementsById(challengeAchievementResponse.achievementId)
                    if (achievementEvent is Event.Success) {
                        achievements.add(achievementEvent.data)
                    } else {
                        return Event.Failure("Not found achievements")
                    }
                }
                Event.Success(achievements)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    private suspend fun getAchievementsById(achievementId: String): Event<Achievement> {
        val idQuery = "objectId=\'$achievementId\'"
        val event = doCall {
            return@doCall achievementApiService.fetchAchievementById(idQuery)
        }
        return getAllAchievements(event)
    }

    private fun getAllAchievements(
        event: Event<List<AchievementResponse>>
    ): Event<Achievement> {
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

    override suspend fun insertChallengeAchievement(
        challengeId: String, achievementId: String
    ): Event<ChallengeAchievement> {
        val event = doCall {
            val request = ChallengeAchievementRequest(challengeId, achievementId)
            return@doCall challengeAchievementApiService.insertChallengeAchievement(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val challengeAchievementTransformer = ChallengeAchievementTransformer()
                val challengeAchievement = challengeAchievementTransformer.fromResponse(response)
                Event.Success(challengeAchievement)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}