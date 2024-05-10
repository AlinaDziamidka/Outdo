package com.example.graduationproject.data.local.transformer

import com.example.graduationproject.data.local.database.model.AchievementModel
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementStatus
import com.example.graduationproject.domain.entity.AchievementType


class AchievementTransformer {

    fun fromModel(model: AchievementModel): Achievement {
        return Achievement(
            achievementId = model.achievementId,
            achievementName = model.achievementName,
            description = model.description,
            achievementStatus = AchievementStatus.valueOf(model.achievementStatus),
            achievementType = AchievementType.valueOf(model.achievementType),
            categoryId = model.categoryId,
            endTime = model.endTime,
            achievementIcon = model.achievementIcon
        )
    }

    fun toModel(achievement: Achievement): AchievementModel {
        return AchievementModel(
            achievementId = achievement.achievementId,
            achievementName = achievement.achievementName,
            description = achievement.description,
            achievementStatus = achievement.achievementStatus.stringValue,
            achievementType = achievement.achievementType.stringValue,
            categoryId = achievement.categoryId,
            endTime = achievement.endTime,
            achievementIcon = achievement.achievementIcon
        )
    }
}