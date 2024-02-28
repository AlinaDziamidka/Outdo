package com.example.graduationproject.data.remote.api.request

import androidx.room.ColumnInfo

data class UserCompetitionRequest (
    val userId: Long,
    val competitionId: Long
)