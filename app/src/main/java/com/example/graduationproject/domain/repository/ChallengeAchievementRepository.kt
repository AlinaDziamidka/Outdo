package com.example.graduationproject.domain.repository

import com.example.graduationproject.domain.entity.ChallengeAchievement
import kotlinx.coroutines.flow.Flow

interface ChallengeAchievementRepository {

    suspend fun fetchAllAchievementsByChallengeId(challengeId: Long): Flow<List<ChallengeAchievement>>
}