package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("objectId")
    val userId: String,
    @SerializedName("email")
    val userIdentity: String,
    @SerializedName("username")
    val username: String?,
)