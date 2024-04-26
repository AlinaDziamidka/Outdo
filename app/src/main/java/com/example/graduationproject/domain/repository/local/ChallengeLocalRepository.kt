package com.example.graduationproject.domain.repository.local

import com.example.graduationproject.data.local.database.model.ChallengeModel
import com.example.graduationproject.domain.entity.Challenge

interface ChallengeLocalRepository {

    suspend fun fetchAll(): List<Challenge>

    suspend fun fetchById(challengeId: String): Challenge

    suspend fun fetchWeekChallenge(challengeType: String): Challenge

    suspend fun insertOne(challenge: Challenge)

    suspend fun deleteById(challengeId: String)

    suspend fun updateOne(challenge: Challenge)
}
