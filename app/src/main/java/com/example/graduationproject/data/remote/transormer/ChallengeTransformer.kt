package com.example.graduationproject.data.remote.transormer

import com.example.graduationproject.data.remote.api.response.ChallengeResponse
import com.example.graduationproject.domain.entity.Challenge

class ChallengeTransformer {
    fun fromResponse(response: ChallengeResponse): Challenge {
        return Challenge(
            challengeId = response.challengeId,
            challengeName = response.challengeName,
            categoryId = response.categoryId,
            challengeType = response.challengeType
        )
    }
}