package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class UsernameRequest(
    @SerializedName("username")
    val username: String,
)