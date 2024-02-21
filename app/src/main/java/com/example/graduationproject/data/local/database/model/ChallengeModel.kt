package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("challenges")
data class ChallengeModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("challenge_id")
    val challengeId: Long,
    @ColumnInfo("challenge_name")
    val challengeName: String,
    @ColumnInfo("category_id")
    val categoryId: Long
)
