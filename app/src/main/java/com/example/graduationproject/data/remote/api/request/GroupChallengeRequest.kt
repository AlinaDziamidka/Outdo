package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class GroupChallengeRequest(
    @SerializedName("groupId")
    val groupId: String,
    @SerializedName("challengeId")
    val challengeId: String
)
