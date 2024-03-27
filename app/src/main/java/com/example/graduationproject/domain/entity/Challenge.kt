package com.example.graduationproject.domain.entity

data class Challenge(
    val challengeId: Long,
    val challengeName: String,
    val categoryId: Long,
    val challengeType: String
)
