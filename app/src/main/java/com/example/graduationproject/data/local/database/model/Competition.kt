package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.nio.file.Path
import java.time.LocalDateTime

@Entity("competitions")
data class Competition(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("competition_id")
    val competitionId : Long,
    @ColumnInfo("competition_name")
    val competitionName: String,
    @ColumnInfo("user_creator_id")
    val creatorId : Long,
    @ColumnInfo("competition_avatar")
    val competitionAvatar : Path,
    @ColumnInfo("end_term")
    val endTerm: LocalDateTime
)
