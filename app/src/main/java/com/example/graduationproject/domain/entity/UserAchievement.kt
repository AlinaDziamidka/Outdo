package com.example.graduationproject.domain.entity

data class UserAchievement(
    val userId: String,
    val achievementId: String,
    val achievementStatus: AchievementStatus,
    val achievementType: AchievementType,
    val photoUrl: String?
)
