package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class ChallengeAchievementRequest(
    @SerializedName("challengeId")
    val challengeId: String,
    @SerializedName("achievementId")
    val achievementId: String
)
