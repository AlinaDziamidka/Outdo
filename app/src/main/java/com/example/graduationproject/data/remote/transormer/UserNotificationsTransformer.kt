package com.example.graduationproject.data.remote.transormer

import com.example.graduationproject.data.remote.api.response.UserNotificationsResponse
import com.example.graduationproject.domain.entity.UserNotifications

class UserNotificationsTransformer {

    fun fromResponse(response: UserNotificationsResponse): UserNotifications {
        return UserNotifications(
            userId = response.userId,
            creatorId = response.creatorId,
            groupId = response.groupId,
            created = response.created
        )
    }
}