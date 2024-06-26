package com.example.graduationproject.data.remote.api.request

data class PushNotificationRequest(
    val message: String,
    val pushSinglecast: List<String>,
    val headers: Map<String, String>
)
