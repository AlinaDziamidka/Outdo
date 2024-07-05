package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class UserNotificationsResponse(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("creatorId")
    val creatorId: String,
    @SerializedName("groupId")
    val groupId: String,
    @SerializedName("created")
    val created: Long
)
