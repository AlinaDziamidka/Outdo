package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class UserAchievementResponse(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("achievementId")
    val achievementId: String,
    @SerializedName("status")
    val achievementStatus: String,
    @SerializedName("achievementType")
    val achievementType: String
)
