package com.example.graduationproject.data.transormer

import com.example.graduationproject.data.remote.api.response.ChallengeAchievementResponse
import com.example.graduationproject.domain.entity.ChallengeAchievement

class ChallengeAchievementTransformer {
    fun fromResponse(response: ChallengeAchievementResponse): ChallengeAchievement {
        return ChallengeAchievement(
            challengeId = response.challengeId,
            achievementId = response.achievementId
        )
    }
}