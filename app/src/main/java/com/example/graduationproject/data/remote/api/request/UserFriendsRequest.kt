package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class UserFriendsRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("friendId")
    val friendId: String
)
