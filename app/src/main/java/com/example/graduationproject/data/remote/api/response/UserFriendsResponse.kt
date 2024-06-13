package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class UserFriendsResponse(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("friendId")
    val friendId: String
)
