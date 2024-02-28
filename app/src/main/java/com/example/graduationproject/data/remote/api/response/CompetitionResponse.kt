package com.example.graduationproject.data.remote.api.response

data class CompetitionResponse(
    val competitionId: Long,
    val competitionName: String,
    val creatorId: Long,
    val competitionAvatarPath: String,
)