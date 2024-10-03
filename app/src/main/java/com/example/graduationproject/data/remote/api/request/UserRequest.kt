package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("userIdentity")
    val userIdentity: String,
    @SerializedName("userPassword")
    val userPassword: String,
    @SerializedName("userAvatarPath")
    val userAvatarPath: String?
)
