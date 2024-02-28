package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.ChallengeAchievementApiService
import com.example.graduationproject.data.transormer.ChallengeAchievementTransformer
import com.example.graduationproject.domain.entity.ChallengeAchievement
import com.example.graduationproject.domain.repository.ChallengeAchievementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ChallengeAchievementRepositoryImpl(private val challengeAchievementApiService: ChallengeAchievementApiService) :
    ChallengeAchievementRepository {


    private val challengeAchievementTransformer = ChallengeAchievementTransformer()

    override suspend fun fetchAllAchievementsByChallengeId(challengeId: Long): Flow<List<ChallengeAchievement>> =
        flow {
            val response =
                challengeAchievementApiService.fetchAllAchievementsByChallengeId(challengeId)
            emit(response)
        }.map { responses ->
            responses.map { response ->
                challengeAchievementTransformer.fromResponse(response)
            }
        }
}