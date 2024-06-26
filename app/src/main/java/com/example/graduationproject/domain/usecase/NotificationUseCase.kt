package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.remote.DeviceRegistrationRepository
import com.example.graduationproject.domain.repository.remote.PushNotificationRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.NonReturningUseCase
import javax.inject.Inject

class NotificationUseCase @Inject constructor(
    private val deviceRegistrationRepository: DeviceRegistrationRepository,
    private val pushNotificationRepository: PushNotificationRepository
) : NonReturningUseCase<NotificationUseCase.Params> {

    data class Params(
        val friends: MutableList<UserProfile>,
        val message: String,
        val title: String
    )

    override suspend fun invoke(params: Params) {
        val friends = params.friends
        val message = params.message
        val title = params.title

        val deviceIds = mutableListOf<String>()

        friends.forEach { userProfile ->
            val deviceEvent = deviceRegistrationRepository.getDeviceId(userProfile.userId)
            if (deviceEvent is Event.Success) {
                deviceIds.add(deviceEvent.data)
            }
        }

        if (deviceIds.isNotEmpty()) {
            val event = pushNotificationRepository.sendPushNotification(deviceIds, message, title)
            if (event is Event.Failure) {
                Log.e("NotificationUseCase", "Failed to send push notification: ${event.exception}")
            }
        }
        else {
            Log.e("NotificationUseCase", "Device Ids are empty")
        }
    }
}