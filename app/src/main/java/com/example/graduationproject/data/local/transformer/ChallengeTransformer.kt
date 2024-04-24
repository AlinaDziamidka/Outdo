package com.example.graduationproject.data.local.transformer

import com.example.graduationproject.data.local.database.model.ChallengeModel
import com.example.graduationproject.domain.entity.Challenge

public class ChallengeTransformer {
    fun fromModel(model: ChallengeModel): Challenge {
        return Challenge(
            challengeId = model.challengeId,
            challengeName = model.challengeName,
            categoryId = model.categoryId,
            challengeType = model.challengeType,
            endTime = model.endTime
        )
    }

    fun toModel(challenge: Challenge): ChallengeModel {
        return ChallengeModel(
            challengeId = challenge.challengeId,
            challengeName = challenge.challengeName,
            categoryId = challenge.categoryId,
            challengeType = challenge.challengeType,
            endTime = challenge.endTime
        )
    }
}