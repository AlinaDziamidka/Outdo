package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.util.Event

interface PushNotificationRepository {
    suspend fun sendPushNotification(deviceIds: List<String>, message: String): Event<String>
}