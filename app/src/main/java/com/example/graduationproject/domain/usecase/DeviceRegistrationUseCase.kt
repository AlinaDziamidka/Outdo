package com.example.graduationproject.domain.usecase


import android.util.Log
import com.example.graduationproject.domain.entity.UserDevice
import com.example.graduationproject.domain.repository.remote.DeviceRegistrationRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.NonReturningUseCase
import javax.inject.Inject

class DeviceRegistrationUseCase @Inject constructor(
    private val deviseRegistrationRepository: DeviceRegistrationRepository
) : NonReturningUseCase<DeviceRegistrationUseCase.Params> {

    data class Params(
        val userId: String,
        val registrationId: String?
    )

    override suspend fun invoke(params: Params) {
        val userId = params.userId
        val registrationId = params.registrationId
        val userDevices = getUserDevices(userId)

        if (registrationId.isNullOrEmpty() || userDevices is Event.Failure) {
            deviseRegistrationRepository.registerDevice(userId)
        }
    }


    private suspend fun getUserDevices(userId: String): Event<List<UserDevice>> {
        val userDeviceEvent = deviseRegistrationRepository.getRegisteredDevice(userId)
        return when (userDeviceEvent) {
            is Event.Success -> {
                Log.d("DeviceRegistrationUseCase", "Received userDevices: ${userDeviceEvent.data}")
                Event.Success(userDeviceEvent.data)
            }

            is Event.Failure -> {
                Log.e(
                    "RemoteLoadManager",
                    "Failed to fetch user device: ${userDeviceEvent.exception}"
                )
                val error = userDeviceEvent.exception
                Event.Failure(error)
            }
        }
    }
}