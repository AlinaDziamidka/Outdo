package com.example.graduationproject.domain.repository.local

import com.example.graduationproject.data.local.database.model.AchievementModel
import com.example.graduationproject.domain.entity.Achievement

interface AchievementLocalRepository {

   suspend fun fetchAll(): List<Achievement>

    suspend fun fetchById(achievementId: String): Achievement

    suspend fun fetchDailyAchievement(achievementType: String): Achievement

    suspend fun insertOne(achievement: Achievement)

    fun deleteById(achievementId: String)

    fun updateOne(achievement: Achievement)
}