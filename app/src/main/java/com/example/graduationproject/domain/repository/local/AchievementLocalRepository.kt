package com.example.graduationproject.domain.repository.local

import com.example.graduationproject.data.local.database.model.AchievementModel
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.util.Event

interface AchievementLocalRepository {

   suspend fun fetchAll(): List<Achievement>

    suspend fun fetchById(achievementId: String): Achievement

    suspend fun fetchDailyAchievement(achievementType: String): Event<Achievement>

    suspend fun insertOne(achievement: Achievement)

    fun deleteById(achievementId: String)

    fun updateOne(achievement: Achievement)
}