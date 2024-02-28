package com.example.graduationproject.domain.entity

import com.example.graduationproject.data.local.database.model.AchievementStatus

data class Achievement(
    val achievementId: Long,
    val achievementName: String,
    val description: String,
    val achievementStatus: AchievementStatus
)
