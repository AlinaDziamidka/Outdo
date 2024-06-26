package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class DeviceRegistrationRequest(
    @SerializedName("deviceToken")
    val deviceToken: String,
    @SerializedName("deviceId")
    val deviceId: String,
    @SerializedName("os")
    val os: String,
    @SerializedName("osVersion")
    val osVersion: String,
    @SerializedName("channels")
    val channels: List<String>? = null
)
