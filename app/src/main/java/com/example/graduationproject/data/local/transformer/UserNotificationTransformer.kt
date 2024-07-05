package com.example.graduationproject.data.local.transformer

import com.example.graduationproject.data.local.database.model.UserNotificationModel
import com.example.graduationproject.domain.entity.UserNotifications

class UserNotificationTransformer {

    fun fromModel(model: UserNotificationModel): UserNotifications {
        return UserNotifications(
            userId = model.userId,
            creatorId = model.creatorId,
            groupId = model.groupId,
            created = model.created
        )
    }

    fun toModel(userNotification: UserNotifications): UserNotificationModel {
        return UserNotificationModel(
            userId = userNotification.userId,
            creatorId = userNotification.creatorId,
            groupId = userNotification.groupId,
            created = userNotification.created
        )
    }
}