package com.example.graduationproject.domain.repository.local

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.util.Event

interface ChallengeLocalRepository {

    suspend fun fetchAll(): List<Challenge>

    suspend fun fetchById(challengeId: String): Challenge

    suspend fun fetchWeekChallenge(challengeType: String): Event<Challenge>

    suspend fun fetchChallengeSByStatusAndId(challengeId: String, challengeStatus: String): Event<Challenge>

    suspend fun insertOne(challenge: Challenge)

    suspend fun deleteById(challengeId: String)

    suspend fun updateOne(challenge: Challenge)
}
