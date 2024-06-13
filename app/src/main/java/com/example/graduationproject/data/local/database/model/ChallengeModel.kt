package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("challenges")
data class ChallengeModel(
    @PrimaryKey
    @ColumnInfo("id")
    val challengeId: String,
    @ColumnInfo("name")
    val challengeName: String,
    @ColumnInfo("category_id")
    val categoryId: String?,
    @ColumnInfo("challengeType")
    val challengeType: String,
    @ColumnInfo("description")
    val challengeDescription: String?,
    @ColumnInfo("endTime")
    val endTime: Long,
    @ColumnInfo("iconPath")
    val challengeIcon: String?,
    @ColumnInfo("challengeStatus")
    val challengeStatus: String,
    @ColumnInfo("creatorId")
    val creatorId: String?
)
