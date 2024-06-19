package com.example.graduationproject.domain.repository.remote
import com.example.graduationproject.domain.entity.UserDevice
import com.example.graduationproject.domain.util.Event

interface DeviceRegistrationRepository {

    suspend fun registerDevice(userId : String): Event<UserDevice>

    suspend fun saveRegistrationId(registrationId: String)

    suspend fun getRegisteredDevice(userIdQuery: String): Event<List<UserDevice>>
}