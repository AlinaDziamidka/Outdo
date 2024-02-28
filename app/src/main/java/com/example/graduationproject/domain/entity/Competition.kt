package com.example.graduationproject.domain.entity

data class Competition (
    val competitionId: Long,
    val competitionName: String,
    val creatorId: Long,
    val competitionAvatarPath: String,
)