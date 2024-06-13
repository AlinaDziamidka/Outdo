package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class UserAchievementRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("achievementId")
    val achievementId: String,
    @SerializedName("status")
    val achievementStatus: String,
    @SerializedName("achievementType")
    val achievementType: String
)
