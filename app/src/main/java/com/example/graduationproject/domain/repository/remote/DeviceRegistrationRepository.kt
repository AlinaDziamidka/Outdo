package com.example.graduationproject.domain.repository.remote
import com.example.graduationproject.data.remote.api.response.DeviceIdResponse
import com.example.graduationproject.domain.entity.UserDevice
import com.example.graduationproject.domain.util.Event
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DeviceRegistrationRepository {

    suspend fun registerDevice()

    suspend fun saveRegistrationId(registrationId: String)

    suspend fun getRegisteredDevice(): Event<String>

    suspend fun getDeviceId(userIdQuery: String): Event<String>
}