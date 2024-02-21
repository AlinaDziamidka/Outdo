package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("user_competitions", primaryKeys = ["user_id", "competition_id"])
data class UserCompetitionModel (
    @ColumnInfo("user_id")
    val userId: Long,
    @ColumnInfo("competition_id")
    val competitionId: Long
)