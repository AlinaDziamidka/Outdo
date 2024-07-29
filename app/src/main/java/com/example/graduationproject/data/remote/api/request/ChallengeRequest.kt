package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ChallengeRequest(
    @SerializedName("name")
    val challengeName: String,
    @SerializedName("categoryId")
    val categoryId: String?,
    @SerializedName("challengeType")
    val challengeType: String,
    @SerializedName("description")
    val challengeDescription: String,
    @SerializedName("endTime")
    val endTime: Long,
    @SerializedName("startTime")
    val startTime: Long,
    @SerializedName("iconPath")
    val challengeIcon: String?,
    @SerializedName("challengeStatus")
    val challengeStatus: String,
    @SerializedName("creatorId")
    val creatorId: String?
)
