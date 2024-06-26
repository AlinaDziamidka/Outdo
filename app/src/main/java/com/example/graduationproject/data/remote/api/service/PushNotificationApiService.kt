package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.request.PushNotificationRequest
import com.example.graduationproject.data.remote.api.response.PushNotificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PushNotificationApiService {

    @POST("messaging/{channel-name}")
    suspend fun sendPushNotification(
        @Path("channel-name") channelName: String,
        @Body request: PushNotificationRequest
    ): Response<PushNotificationResponse>
}