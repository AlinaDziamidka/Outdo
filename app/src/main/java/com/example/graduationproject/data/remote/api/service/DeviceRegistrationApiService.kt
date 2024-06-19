package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.request.DeviceRegistrationRequest
import com.example.graduationproject.data.remote.api.response.DeviceRegistrationResponse
import com.example.graduationproject.data.remote.api.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DeviceRegistrationApiService {

    @POST("api/messaging/registrations")
    suspend fun registerDevice(@Body request: DeviceRegistrationRequest): Response<DeviceRegistrationResponse>

    @GET("data/messaging/registrations")
    suspend fun getRegisteredDevice(@Query("where") userIdQuery: String): Response<List<DeviceRegistrationResponse>>
}