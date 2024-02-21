package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("competitions")
data class CompetitionModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("competition_id")
    val competitionId: Long,
    @ColumnInfo("competition_name")
    val competitionName: String,
    @ColumnInfo("user_creator_id")
    val creatorId: Long,
    @ColumnInfo("competition_avatar")
    val competitionAvatarPath: String,
//    @ColumnInfo("end_term")
//    val endTerm: LocalDateTime
)
