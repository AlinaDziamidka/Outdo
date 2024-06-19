package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class DeviceRegistrationResponse(
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
    @SerializedName("objectId")
    val registrationId: String
)