package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

    data class SignInRequest(
        @SerializedName("email")
        val userIdentity: String,
        @SerializedName("password")
        val password: String,
    )
