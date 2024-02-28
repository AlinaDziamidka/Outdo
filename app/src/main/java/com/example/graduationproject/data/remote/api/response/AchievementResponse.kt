package com.example.graduationproject.data.remote.api.response

import com.example.graduationproject.data.local.database.model.AchievementStatus

data class AchievementResponse(
    val achievementId: Long,
    val achievementName: String,
    val description: String,
    val achievementStatus: AchievementStatus
)
