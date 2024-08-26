package com.example.graduationproject.data.local.transformer

import com.example.graduationproject.data.local.database.model.UserAchievementModel
import com.example.graduationproject.domain.entity.AchievementStatus
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.entity.UserAchievement

class UserAchievementTransformer {

    fun fromModel(model: UserAchievementModel): UserAchievement {
        return UserAchievement(
            achievementId = model.achievementId,
            userId = model.userId,
            achievementStatus = AchievementStatus.valueOf(model.achievementStatus),
            achievementType = AchievementType.valueOf(model.achievementType),
            photoUrl = model.photoUrl
        )
    }

    fun toModel(userAchievement: UserAchievement): UserAchievementModel {
        return UserAchievementModel(
            achievementId = userAchievement.achievementId,
            userId = userAchievement.userId,
            achievementStatus = userAchievement.achievementStatus.stringValue,
            achievementType = userAchievement.achievementType.stringValue,
            photoUrl = userAchievement.photoUrl
        )
    }
}