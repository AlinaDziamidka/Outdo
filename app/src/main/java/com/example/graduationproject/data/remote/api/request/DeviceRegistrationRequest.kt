package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class DeviceRegistrationRequest(
    @SerializedName("deviceToken")
    val deviceToken: String,
    @SerializedName("deviceId")
    val deviceId: String,
    @SerializedName("operatingSystemName")
    val os: String,
    @SerializedName("operatingSystemVersion")
    val osVersion: String,
    @SerializedName("channelName")
    val channels: List<String>? = null,
    @SerializedName("expiration")
    val expiration: Long? = null,
    @SerializedName("userId")
    val userId: String
)
