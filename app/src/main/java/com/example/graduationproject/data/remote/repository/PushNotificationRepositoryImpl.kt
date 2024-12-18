package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.request.PushNotificationRequest
import com.example.graduationproject.data.remote.api.service.PushNotificationApiService
import com.example.graduationproject.domain.repository.remote.PushNotificationRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class PushNotificationRepositoryImpl @Inject constructor(
    private val pushNotificationApiService: PushNotificationApiService
) : PushNotificationRepository {

    override suspend fun sendPushNotification(
        deviceIds: List<String>,
        message: String
    ): Event<String> {

        val event = doCall {
            val request = PushNotificationRequest(
                message = message,
                pushSinglecast = deviceIds,
                headers = mapOf(
                    "android-ticker-text" to "You just got a private push notification!",
                    "android-content-title" to "",
                    "android-content-text" to message
                )
            )
            return@doCall pushNotificationApiService.sendPushNotification("default", request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                Event.Success(response.status)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}