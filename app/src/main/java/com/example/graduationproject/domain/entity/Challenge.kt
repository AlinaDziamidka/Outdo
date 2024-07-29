package com.example.graduationproject.domain.entity


data class Challenge(
    val challengeId: String,
    val challengeName: String,
    val categoryId: String?,
    val challengeType: ChallengeType,
    val challengeDescription: String?,
    val endTime: Long,
    val startTime: Long?,
    val challengeIcon: String?,
    val challengeStatus: ChallengeStatus,
    val creatorId: String?
)
