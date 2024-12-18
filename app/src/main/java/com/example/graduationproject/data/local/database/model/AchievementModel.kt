package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("achievements")
data class AchievementModel(
    @PrimaryKey()
    @ColumnInfo("id")
    val achievementId: String,
    @ColumnInfo("name")
    val achievementName: String,
    @ColumnInfo("description")
    val description: String?,
    @ColumnInfo("status")
    val achievementStatus: String,
    @ColumnInfo("categoryId")
    val categoryId: String?,
    @ColumnInfo("achievementType")
    val achievementType: String,
    @ColumnInfo("endTime")
    val endTime: Long,
    @ColumnInfo("iconPath")
    val achievementIcon: String?
)
