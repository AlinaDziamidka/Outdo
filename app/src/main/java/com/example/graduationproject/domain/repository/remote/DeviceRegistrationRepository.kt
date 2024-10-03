package com.example.graduationproject.domain.repository.remote
import com.example.graduationproject.domain.util.Event

interface DeviceRegistrationRepository {

    suspend fun registerDevice()

    suspend fun saveRegistrationId(registrationId: String)

    suspend fun getRegisteredDevice(): Event<String>

    suspend fun getDeviceId(userIdQuery: String): Event<String>
}