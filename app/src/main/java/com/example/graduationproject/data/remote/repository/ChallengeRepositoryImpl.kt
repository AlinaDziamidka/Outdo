package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.transormer.ChallengeTransformer
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.repository.remote.ChallengeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ChallengeRepositoryImpl(private val challengeApiService: ChallengeApiService) :
    ChallengeRepository {

    override suspend fun fetchChallengesById(challengeIdQuery: String): List<Challenge> {
        val query = "objectId=\'$challengeIdQuery\'"
        val response = challengeApiService.fetchChallengesById(query)
        return response.map { challengeResponse ->
            val challengeTransformer = ChallengeTransformer()
            challengeTransformer.fromResponse(challengeResponse)
        }
    }
}