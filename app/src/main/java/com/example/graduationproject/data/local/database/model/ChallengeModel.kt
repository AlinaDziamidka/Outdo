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
    val categoryId: String,
    @SerializedName("challengeType")
    val challengeType: String,
    @SerializedName("endTime")
    val endTime: Long
)
