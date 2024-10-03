package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.util.Event

interface ChallengeRemoteRepository {
    suspend fun fetchChallengesById(challengeIdQuery: String): Event<Challenge>

    suspend fun fetchWeekChallenge(challengeTypeQuery: String): Event<Challenge>

    suspend fun fetchChallengesByStatusAndId(
        challengeIdQuery: String,
        challengeStatusQuery: String
    ): Event<Challenge>

    suspend fun insertChallenge(
        challengeName: String,
        creatorId: String,
        description: String,
        endTime: Long,
        startTime: Long
    ): Event<Challenge>
}