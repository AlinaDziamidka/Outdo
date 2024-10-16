package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.UserNotifications
import com.example.graduationproject.domain.util.Event

interface UserNotificationsRemoteRepository {

    suspend fun insertNotification(userId: String, creatorId: String, groupId: String): Event<UserNotifications>

    suspend fun fetchNotificationsByUserId(userIdQuery: String): Event<List<UserNotifications>>

    suspend fun deleteNotification(userIdQuery: String, groupIdQuery: String): Event<Long>
}