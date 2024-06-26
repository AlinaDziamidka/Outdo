package com.example.graduationproject.domain.repository.local

import com.example.graduationproject.domain.entity.UserNotifications

interface UserNotificationsLocalRepository {

    suspend fun fetchAll(): List<UserNotifications>

    suspend fun insertOne(userNotification: UserNotifications)

    suspend fun deleteOne(userNotification: UserNotifications)

    suspend fun updateOne(userNotification: UserNotifications)
}