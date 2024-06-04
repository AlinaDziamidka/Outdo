package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.response.AchievementResponse
import com.example.graduationproject.data.remote.api.response.ChallengeResponse
import com.example.graduationproject.data.remote.api.service.AchievementApiService
import com.example.graduationproject.data.remote.api.service.ChallengeAchievementApiService
import com.example.graduationproject.data.remote.transormer.AchievementTransformer
import com.example.graduationproject.data.remote.transormer.ChallengeAchievementTransformer
import com.example.graduationproject.data.remote.transormer.ChallengeTransformer
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeAchievement
import com.example.graduationproject.domain.repository.remote.ChallengeAchievementRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.http.Query

class ChallengeAchievementRemoteRepositoryImpl(
    private val challengeAchievementApiService: ChallengeAchievementApiService,
    private val achievementApiService: AchievementApiService
) :
    ChallengeAchievementRemoteRepository {

    override suspend fun fetchAllAchievementsByChallengeId(challengeIdQuery: String): Event<List<Achievement>> {
        val query = "challengeId=\'$challengeIdQuery\'"
        Log.d("ChallengeAchievementRepositoryImpl", "Fetching all achievements by challenge ID: $challengeIdQuery")
        val event = doCall {
            return@doCall challengeAchievementApiService.fetchAllAchievementsByChallengeId(query)
        }

        return when (event) {
            is Event.Success -> {
                val achievements = mutableListOf<Achievement>()
                val response = event.data

                response.forEach { challengeAchievementResponse ->
                    val achievementEvent = getAchievementsById(challengeAchievementResponse.achievementId)
                    if (achievementEvent is Event.Success) {
                        achievements.add(achievementEvent.data)
                    } else {
                        Log.e(
                            "ChallengeAchievementRepositoryImpl",
                            "Failed to fetch achievement with ID: ${challengeAchievementResponse.challengeId}"
                        )
                        return Event.Failure("Not found achievements")
                    }
                }
                Log.d(
                    "ChallengeAchievementRepositoryImpl",
                    "Successfully fetched all achievements by challenge ID: $challengeIdQuery"
                )
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
        Log.d("ChallengeAchievementRepositoryImpl", "Fetching achievement by ID: $achievementId")
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
                Log.d("ChallengeAchievementRepositoryImpl", "Received achievement: ${event.data}")
                val response = event.data.first()
                val achievementTransformer = AchievementTransformer()
                val achievement = achievementTransformer.fromResponse(response)
                Log.d(
                    "ChallengeAchievementRepositoryImpl",
                    "Transformed response to achievement: $achievement"
                )
                Event.Success(achievement)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}