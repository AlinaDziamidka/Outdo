package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.ChallengeApiService
import com.example.graduationproject.data.transormer.AwardTransformer
import com.example.graduationproject.data.transormer.ChallengeTransformer
import com.example.graduationproject.domain.entity.Award
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.repository.ChallengeRepository

class ChallengeRepositoryImpl(private val challengeApiService: ChallengeApiService) :
    ChallengeRepository {

    private val challengeTransformer = ChallengeTransformer()

    override suspend fun fetchChallenge(challengeId: Long): Challenge {
        val response = challengeApiService.fetchChallenge(challengeId)
        return challengeTransformer.fromResponse(response)
    }
}