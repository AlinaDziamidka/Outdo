package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.util.Event

interface AchievementRemoteRepository {

    suspend fun fetchAchievementById(achievementIdQuery: String): Event<Achievement>

    suspend fun fetchDailyAchievement(achievementTypeQuery: String): Event<Achievement>

    suspend fun insertAchievement(
        achievementName: String,
        description: String,
        endTime: Long
    ): Event<Achievement>
}