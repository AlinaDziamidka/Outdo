package com.example.graduationproject.data.remote.transormer

import com.example.graduationproject.data.remote.api.response.ChallengeResponse
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.entity.ChallengeType

class ChallengeTransformer {
    fun fromResponse(response: ChallengeResponse): Challenge {
        return Challenge(
            challengeId = response.challengeId,
            challengeName = response.challengeName,
            categoryId = response.categoryId,
            challengeType = ChallengeType.valueOf(response.challengeType),
            challengeDescription = response.challengeDescription,
            endTime = response.endTime,
            challengeIcon = response.challengeIcon,
            challengeStatus = ChallengeStatus.valueOf(response.challengeStatus)
        )
    }
}