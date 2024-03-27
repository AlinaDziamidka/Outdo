package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class ChallengeRequest(
    @SerializedName("objectId")
    val challengeId: String,
    @SerializedName("name")
    val challengeName: String,
    @SerializedName("categoryId")
    val categoryId: String
)
