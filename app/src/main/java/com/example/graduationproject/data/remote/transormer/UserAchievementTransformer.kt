package com.example.graduationproject.data.remote.transormer

import com.example.graduationproject.data.remote.api.response.UserAchievementResponse
import com.example.graduationproject.domain.entity.AchievementStatus
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.entity.UserAchievement

class UserAchievementTransformer {

    fun fromResponse(response: UserAchievementResponse): UserAchievement {
        return UserAchievement(
            achievementId = response.achievementId,
            userId = response.userId,
            achievementStatus = AchievementStatus.valueOf(response.achievementStatus),
            achievementType = AchievementType.valueOf(response.achievementType),
            photoUrl = response.photoUrl
        )
    }
}