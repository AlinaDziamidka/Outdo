package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class GroupChallengeResponse(
    @SerializedName("groupId")
    val groupId: String,
    @SerializedName("challengeId")
    val challengeId: String
)
