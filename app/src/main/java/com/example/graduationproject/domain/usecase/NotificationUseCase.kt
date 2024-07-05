package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.remote.DeviceRegistrationRepository
import com.example.graduationproject.domain.repository.remote.PushNotificationRepository
import com.example.graduationproject.domain.repository.remote.UserNotificationsRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.NonReturningUseCase
import javax.inject.Inject

class NotificationUseCase @Inject constructor(
    private val deviceRegistrationRepository: DeviceRegistrationRepository,
    private val pushNotificationRepository: PushNotificationRepository,
    private val userNotificationsRemoteRepository: UserNotificationsRemoteRepository
) : NonReturningUseCase<NotificationUseCase.Params> {

    data class Params(
        val friends: MutableList<UserProfile>,
        val message: String,
        val group: Group,
        val creatorId : String
    )

    override suspend fun invoke(params: Params) {
        val friends = params.friends
        val message = params.message
        val groupId = params.group.groupId
        val creatorId = params.creatorId

        val deviceIds = mutableListOf<String>()
        val registerUsers = mutableListOf<UserProfile>()

        friends.forEach { userFriend ->
            val deviceEvent = deviceRegistrationRepository.getDeviceId(userFriend.userId)
            if (deviceEvent is Event.Success) {
                deviceIds.add(deviceEvent.data)
                registerUsers.add(userFriend)
            }
        }

        if (deviceIds.isNotEmpty()) {
            sendPushNotification(deviceIds, message)
            registerUsers.forEach { registerUser ->
                val notificationEvent = userNotificationsRemoteRepository.insertNotification(registerUser.userId, creatorId, groupId)
                if (notificationEvent is Event.Failure) {
                    Log.e("NotificationUseCase", "Failed to add user notification: ${notificationEvent.exception}")
                }
            }

        } else {
            Log.e("NotificationUseCase", "Device Ids are empty")
        }
    }

    private suspend fun sendPushNotification(
        deviceIds: MutableList<String>,
        message: String
    ) {
        val event = pushNotificationRepository.sendPushNotification(deviceIds, message)
        if (event is Event.Failure) {
            Log.e("NotificationUseCase", "Failed to send push notification: ${event.exception}")
        }
    }
}