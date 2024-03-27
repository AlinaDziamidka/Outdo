package com.example.graduationproject.data.remote.transormer

import com.example.graduationproject.data.remote.api.response.AchievementResponse
import com.example.graduationproject.domain.entity.Achievement

class AchievementTransformer {

    fun fromResponse(response: AchievementResponse): Achievement {
        return Achievement(
            achievementId = response.achievementId,
            achievementName = response.achievementName,
            description = response.description,
            achievementStatus = response.achievementStatus
        )
    }
}