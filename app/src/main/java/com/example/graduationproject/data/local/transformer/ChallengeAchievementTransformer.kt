package com.example.graduationproject.data.local.transformer

import com.example.graduationproject.data.local.database.model.ChallengeAchievementModel
import com.example.graduationproject.domain.entity.ChallengeAchievement

public class ChallengeAchievementTransformer {

    fun fromModel(model: ChallengeAchievementModel): ChallengeAchievement {
        return ChallengeAchievement(
            challengeId = model.challengeId,
            achievementId = model.achievementId
        )
    }

    fun toModel(challengeAchievement: ChallengeAchievement): ChallengeAchievementModel {
        return ChallengeAchievementModel(
            challengeId = challengeAchievement.challengeId,
            achievementId = challengeAchievement.achievementId
        )
    }
}