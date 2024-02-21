package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("achievements")
data class AchievementModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("achievement_id")
    val achievementId : Long,
    @ColumnInfo("achievement_name")
    val achievementName: String,
    val description: String,
    @ColumnInfo("challenge_id")
    val challengeId: Long,
    @ColumnInfo("achievement_status")
    val achievementStatus: AchievementStatus
)
