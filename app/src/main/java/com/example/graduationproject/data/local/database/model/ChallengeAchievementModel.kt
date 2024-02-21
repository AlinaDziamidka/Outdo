package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("challenge_achievements", primaryKeys = ["challenge_id", "achievement_id"])
data class ChallengeAchievementModel(
    @ColumnInfo("challenge_id")
    val challengeId: Long,
    @ColumnInfo("achievement_id")
    val achievementId: Long
)
