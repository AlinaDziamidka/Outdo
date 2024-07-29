package com.example.graduationproject.domain.entity

data class Achievement(
    val achievementId: String,
    val achievementName: String,
    val description: String?,
    val achievementStatus: AchievementStatus,
    val categoryId: String?,
    val achievementType: AchievementType,
    val endTime: Long,
    val achievementIcon: String?
)
