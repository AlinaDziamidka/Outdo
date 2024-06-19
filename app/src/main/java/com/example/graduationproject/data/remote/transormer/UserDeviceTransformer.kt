package com.example.graduationproject.data.remote.transormer

import com.example.graduationproject.data.remote.api.response.DeviceRegistrationResponse
import com.example.graduationproject.domain.entity.UserDevice

class UserDeviceTransformer {

    fun fromResponse(response: DeviceRegistrationResponse): UserDevice {
        return UserDevice(
            deviceToken = response.deviceToken,
            deviceId = response.deviceId,
            os = response.os,
            osVersion = response.osVersion,
            channels = response.channels,
            expiration = response.expiration,
            registrationId = response.registrationId
        )
    }
}