package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.ChallengeAchievement
import com.example.graduationproject.domain.util.Event

interface ChallengeAchievementRemoteRepository {

    suspend fun fetchAllAchievementsByChallengeId(challengeIdQuery: String): Event<List<Achievement>>

    suspend fun insertChallengeAchievement(
        challengeId: String,
        achievementId: String
    ): Event<ChallengeAchievement>
}