package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.request.DeviceRegistrationRequest
import com.example.graduationproject.data.remote.api.response.DeviceIdResponse
import com.example.graduationproject.data.remote.api.response.DeviceRegistrationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface DeviceRegistrationApiService {

    @POST("messaging/registrations")
    suspend fun registerDevice(
        @Header("user-token") userToken: String?,
        @Body request: DeviceRegistrationRequest
    ): Response<DeviceRegistrationResponse>

    @GET("data/deviceRegistration")
    suspend fun getRegisteredDevice(
        @Header("user-token") userToken: String?,
        @Query("where") registrationId: String
    ): Response<List<DeviceRegistrationResponse>>

    @GET("data/deviceRegistration")
    suspend fun getDeviceId(
        @Query("where") userIdQuery: String
    ): Response<List<DeviceIdResponse>>
}