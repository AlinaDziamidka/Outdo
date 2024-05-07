package com.example.graduationproject.domain.entity


data class Challenge(
    val challengeId: String,
    val challengeName: String,
    val categoryId: String,
    val challengeType: ChallengeType,
    val challengeDescription: String?,
    val endTime: Long
)
