package com.example.graduationproject.domain.usecase


import android.util.Log
import com.example.graduationproject.domain.entity.UserDevice
import com.example.graduationproject.domain.repository.remote.DeviceRegistrationRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.NonReturningUseCase
import javax.inject.Inject

class DeviceRegistrationUseCase @Inject constructor(
    private val deviseRegistrationRepository: DeviceRegistrationRepository
) : NonReturningUseCase<NonReturningUseCase.None> {


    override suspend fun invoke(params: NonReturningUseCase.None) {
//        val userDevices = checkUserDevices()

//        if (userDevices is Event.Failure) {
        deviseRegistrationRepository.registerDevice()
//        }
    }


    private suspend fun checkUserDevices(): Event<String> {
        val userDeviceEvent = deviseRegistrationRepository.getRegisteredDevice()
        return when (userDeviceEvent) {
            is Event.Success -> {
                Log.d("DeviceRegistrationUseCase", "Received userDevices: ${userDeviceEvent.data}")
                Event.Success(userDeviceEvent.data)
            }

            is Event.Failure -> {
                Log.e(
                    "DeviceRegistrationUseCase",
                    "Failed to fetch user device: ${userDeviceEvent.exception}"
                )
                val error = userDeviceEvent.exception
                Event.Failure(error)
            }
        }
    }
}