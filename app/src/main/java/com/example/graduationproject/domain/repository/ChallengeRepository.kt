package com.example.graduationproject.domain.repository

import com.example.graduationproject.domain.entity.Challenge

interface ChallengeRepository {
    suspend fun fetchChallenge(challengeId: Long): Challenge
}