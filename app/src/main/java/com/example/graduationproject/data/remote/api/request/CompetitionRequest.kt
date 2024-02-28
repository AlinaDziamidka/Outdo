package com.example.graduationproject.data.remote.api.request

data class CompetitionRequest(
    val competitionId: Long,
    val competitionName: String,
    val creatorId: Long,
    val competitionAvatarPath: String,
)