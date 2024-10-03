package com.example.graduationproject.data.remote.transormer

import com.example.graduationproject.data.remote.api.response.AchievementResponse
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementStatus
import com.example.graduationproject.domain.entity.AchievementType

class AchievementTransformer {

    fun fromResponse(response: AchievementResponse): Achievement {
        return Achievement(
            achievementId = response.achievementId,
            achievementName = response.achievementName,
            description = response.description,
            achievementStatus = AchievementStatus.valueOf(response.achievementStatus),
            achievementType = AchievementType.valueOf(response.achievementType),
            categoryId = response.categoryId,
            endTime = response.endTime,
            achievementIcon = response.achievementIcon
        )
    }
}