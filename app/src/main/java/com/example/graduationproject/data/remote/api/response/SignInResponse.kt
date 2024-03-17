package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("objectId")
    val userId: String,
    @SerializedName("user-token")
    val token: String,
    @SerializedName("email")
    val userIdentity: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatarPath")
    val userAvatarPath: String?
)