package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class ChallengeAchievementResponse(
    @SerializedName("challengeId")
    val challengeId: String,
    @SerializedName("achievementId")
    val achievementId: String
)