package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ChallengeRequest(
    @SerializedName("objectId")
    val challengeId: String,
    @SerializedName("name")
    val challengeName: String,
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("challengeType")
    val challengeType: String,
    @SerializedName("description")
    val challengeDescription: String,
    @SerializedName("endTime")
    val endTime: Long
)
