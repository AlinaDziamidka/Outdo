package com.example.graduationproject.domain.entity

import java.time.LocalDateTime

data class Challenge(
    val challengeId: String,
    val challengeName: String,
    val categoryId: String,
    val challengeType: String,
    val endTime: Long
)
