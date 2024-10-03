package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class PushNotificationResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("messageId")
    val messageId: String?
)
