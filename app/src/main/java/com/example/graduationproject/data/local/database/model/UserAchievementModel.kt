package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("user_achievements", primaryKeys = ["user_id", "achievement_id"])
data class UserAchievementModel(
    @ColumnInfo("user_id")
    val userId: String,
    @ColumnInfo("achievement_id")
    val achievementId: String,
    @ColumnInfo("achievement_status")
    val achievementStatus: String,
    @ColumnInfo("achievement_type")
    val achievementType: String
)
