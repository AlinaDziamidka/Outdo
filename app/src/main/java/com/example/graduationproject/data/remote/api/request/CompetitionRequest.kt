package com.example.graduationproject.data.remote.api.request

data class CompetitionRequest(
    val competitionId: String,
    val competitionName: String,
    val creatorId: String,
    val competitionAvatarPath: String,
)