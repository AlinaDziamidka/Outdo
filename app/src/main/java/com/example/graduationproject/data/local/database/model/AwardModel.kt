package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("awards")
data class AwardModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("award_id")
    val awardId: Long,
    @ColumnInfo("award_name")
    val awardName: String,
    @ColumnInfo("award_icon")
    val awardIconPath: String
)