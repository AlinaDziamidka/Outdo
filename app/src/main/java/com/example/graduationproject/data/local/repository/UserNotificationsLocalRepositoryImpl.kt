package com.example.graduationproject.data.local.repository

import com.example.graduationproject.data.local.database.dao.UserNotificationDao
import com.example.graduationproject.data.local.transformer.UserNotificationTransformer
import com.example.graduationproject.domain.entity.UserNotifications
import com.example.graduationproject.domain.repository.local.UserNotificationsLocalRepository
import javax.inject.Inject

class UserNotificationsLocalRepositoryImpl @Inject constructor(private val userNotificationDao: UserNotificationDao) :
    UserNotificationsLocalRepository {

    private val userNotificationTransformer = UserNotificationTransformer()

    override suspend fun fetchAll(): List<UserNotifications> {
        val model = userNotificationDao.fetchAll()
        return model.map {
            userNotificationTransformer.fromModel(it)
        }
    }

    override suspend fun insertOne(userNotification: UserNotifications) {
        val model = userNotificationTransformer.toModel(userNotification)
        return userNotificationDao.insertOne(model)
    }

    override suspend fun deleteById(userId: String, groupId: String) =
        userNotificationDao.deleteById(userId, groupId)

    override suspend fun updateOne(userNotification: UserNotifications) {
        val model = userNotificationTransformer.toModel(userNotification)
        return userNotificationDao.updateOne(model)
    }
}