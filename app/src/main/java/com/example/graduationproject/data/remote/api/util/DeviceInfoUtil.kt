package com.example.graduationproject.data.remote.api.util

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

object DeviceInfoUtil {

    fun getDeviceId(): String =
        UUID.randomUUID().toString()

    suspend fun getDeviceToken(): String {
        return withContext(Dispatchers.IO) {
            FirebaseMessaging.getInstance().token.await()
        }
    }
}