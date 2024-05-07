package com.example.graduationproject.data.remote.api.response

import com.example.graduationproject.domain.entity.AchievementStatus
import com.google.gson.annotations.SerializedName

data class AchievementResponse(
    @SerializedName("objectId")
    val achievementId: String,
    @SerializedName("name")
    val achievementName: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("status")
    val achievementStatus: String,
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("achievementType")
    val achievementType: String,
    @SerializedName("endTime")
    val endTime: Long
)
