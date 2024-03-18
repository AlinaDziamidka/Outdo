package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("objectId")
    val userId: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val userIdentity: String,
    @SerializedName("avatarPath")
    val userAvatarPath: String?
)

