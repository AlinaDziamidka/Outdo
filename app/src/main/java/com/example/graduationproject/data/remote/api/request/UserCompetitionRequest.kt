package com.example.graduationproject.data.remote.api.request

import androidx.room.ColumnInfo

data class UserCompetitionRequest (
    val userId: String,
    val competitionId: String
)