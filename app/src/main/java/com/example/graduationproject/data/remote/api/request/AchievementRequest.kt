package com.example.graduationproject.data.remote.api.request

import com.example.graduationproject.data.local.database.model.AchievementStatus

data class AchievementRequest(
    val achievementId: String,
    val achievementName: String,
    val description: String,
    val achievementStatus: AchievementStatus
)
