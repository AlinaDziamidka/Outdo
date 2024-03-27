package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Challenge
import kotlinx.coroutines.flow.Flow

interface ChallengeRepository {
    suspend fun fetchChallengesById(challengeIdQuery: String): Flow<List<Challenge>>
}