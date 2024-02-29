package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.transormer.ChallengeTransformer
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.repository.ChallengeRepository

class ChallengeRepositoryImpl(private val challengeApiService: ChallengeApiService) :
    ChallengeRepository {

    override suspend fun fetchChallenge(challengeId: Long): Challenge {
        val response = challengeApiService.fetchChallenge(challengeId)
        val challengeTransformer = ChallengeTransformer()
        return challengeTransformer.fromResponse(response)
    }
}