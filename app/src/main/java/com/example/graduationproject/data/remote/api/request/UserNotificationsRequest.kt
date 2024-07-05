package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class UserNotificationsRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("creatorId")
    val creatorId: String,
    @SerializedName("groupId")
    val groupId: String
)
