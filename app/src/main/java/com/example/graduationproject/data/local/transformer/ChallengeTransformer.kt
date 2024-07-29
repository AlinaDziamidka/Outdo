package com.example.graduationproject.data.local.transformer

import com.example.graduationproject.data.local.database.model.ChallengeModel
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.entity.ChallengeType

public class ChallengeTransformer {
    fun fromModel(model: ChallengeModel): Challenge {
        return Challenge(
            challengeId = model.challengeId,
            challengeName = model.challengeName,
            categoryId = model.categoryId,
            challengeType = ChallengeType.valueOf(model.challengeType),
            challengeDescription = model.challengeDescription,
            endTime = model.endTime,
            startTime = model.startTime,
            challengeIcon = model.challengeIcon,
            challengeStatus = ChallengeStatus.valueOf(model.challengeStatus),
            creatorId = model.creatorId
        )
    }

    fun toModel(challenge: Challenge): ChallengeModel {
        return ChallengeModel(
            challengeId = challenge.challengeId,
            challengeName = challenge.challengeName,
            categoryId = challenge.categoryId,
            challengeType = challenge.challengeType.stringValue,
            challengeDescription = challenge.challengeDescription,
            endTime = challenge.endTime,
            startTime = challenge.startTime,
            challengeIcon = challenge.challengeIcon,
            challengeStatus = challenge.challengeStatus.stringValue,
            creatorId = challenge.creatorId
        )
    }
}