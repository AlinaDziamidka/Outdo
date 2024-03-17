package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class UsernameResponse(
    @SerializedName("username")
    val username: String?
)
