package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Achievement

interface AchievementRepository {
    suspend fun fetchAchievement(achievementId: Long): Achievement
}