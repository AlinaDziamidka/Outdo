package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.ChallengeAchievementApiService
import com.example.graduationproject.data.remote.transormer.ChallengeAchievementTransformer
import com.example.graduationproject.domain.entity.ChallengeAchievement
import com.example.graduationproject.domain.repository.remote.ChallengeAchievementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ChallengeAchievementRepositoryImpl(private val challengeAchievementApiService: ChallengeAchievementApiService) :
    ChallengeAchievementRepository {

    override suspend fun fetchAllAchievementsByChallengeId(challengeId: Long): Flow<List<ChallengeAchievement>> =
        flow {
            val response =
                challengeAchievementApiService.fetchAllAchievementsByChallengeId(challengeId)
            emit(response)
        }.map { responses ->
            responses.map { response ->
                val challengeAchievementTransformer = ChallengeAchievementTransformer()
                challengeAchievementTransformer.fromResponse(response)
            }
        }
}