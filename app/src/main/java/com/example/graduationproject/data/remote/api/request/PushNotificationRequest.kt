package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class PushNotificationRequest(
    @SerializedName("message")
    val message: String,
    @SerializedName("pushSinglecast")
    val pushSinglecast: List<String>,
    @SerializedName("headers")
    val headers: Map<String, String>
)
