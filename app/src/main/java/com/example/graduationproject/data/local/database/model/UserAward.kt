package com.example.graduationproject.data.local.database.model

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("user_awards", primaryKeys = ["user_id", "award_id"])
data class UserAward(
    @ColumnInfo("user_id")
    val userId: Long,
    @ColumnInfo("award_id")
    val awardId: Long
)
