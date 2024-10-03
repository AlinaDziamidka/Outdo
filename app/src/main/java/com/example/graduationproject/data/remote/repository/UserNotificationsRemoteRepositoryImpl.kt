package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.request.UserNotificationsRequest
import com.example.graduationproject.data.remote.api.service.UserNotificationsApiService
import com.example.graduationproject.data.remote.transormer.UserNotificationsTransformer
import com.example.graduationproject.domain.entity.UserNotifications
import com.example.graduationproject.domain.repository.remote.UserNotificationsRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class UserNotificationsRemoteRepositoryImpl @Inject constructor(
    private val userNotificationsApiService: UserNotificationsApiService
) : UserNotificationsRemoteRepository {

    private val userNotificationsTransformer = UserNotificationsTransformer()

    override suspend fun insertNotification(
        userId: String,
        creatorId: String,
        groupId: String
    ): Event<UserNotifications> {
        val event = doCall {
            val request = UserNotificationsRequest(userId, creatorId, groupId)
            return@doCall userNotificationsApiService.insertNotification(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val userNotification = userNotificationsTransformer.fromResponse(response)
                Event.Success(userNotification)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun fetchNotificationsByUserId(userIdQuery: String): Event<List<UserNotifications>> {
        val query = "userId=\'$userIdQuery\'"
        val event = doCall {
            return@doCall userNotificationsApiService.fetchNotificationsByUserId(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val userNotifications = response.map { userNotificationResponse ->
                    userNotificationsTransformer.fromResponse(userNotificationResponse)
                }
                Event.Success(userNotifications)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun deleteNotification(
        userIdQuery: String,
        groupIdQuery: String
    ): Event<Long> {
        val query = "userId='$userIdQuery' AND groupId='$groupIdQuery'"

        val event = doCall {
            return@doCall userNotificationsApiService.deleteNotification(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                Event.Success(response)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}