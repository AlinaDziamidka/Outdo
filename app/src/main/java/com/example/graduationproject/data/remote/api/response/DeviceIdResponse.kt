package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class DeviceIdResponse(
    @SerializedName("deviceId")
    val deviceId: String
)