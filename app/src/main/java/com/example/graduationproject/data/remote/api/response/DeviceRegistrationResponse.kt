package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class DeviceRegistrationResponse(
    @SerializedName("registrationId")
    val registrationId: String
)