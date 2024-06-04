package com.example.graduationproject.domain.repository.local

import com.example.graduationproject.domain.entity.ChallengeAchievement

interface ChallengeAchievementsLocalRepository {

   suspend fun fetchAll(): List<ChallengeAchievement>

   suspend fun fetchAchievementsByChallengeId(challengeId: String): List<ChallengeAchievement>

   suspend fun fetchChallengesByChallengeId(challengeId: String): List<ChallengeAchievement>

   suspend fun insertOne(challengeAchievement: ChallengeAchievement)

   suspend fun deleteOne(challengeAchievement: ChallengeAchievement)

   suspend fun updateOne(challengeAchievement: ChallengeAchievement)
}