package com.example.graduationproject.domain.usecase

import com.example.graduationproject.domain.repository.local.UserNotificationsLocalRepository
import com.example.graduationproject.domain.repository.remote.UserNotificationsRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.NonReturningUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteNotificationUseCase @Inject constructor(
    private val userNotificationsRemoteRepository: UserNotificationsRemoteRepository,
    private val userNotificationsLocalRepository: UserNotificationsLocalRepository
) : NonReturningUseCase<DeleteNotificationUseCase.Params> {

    data class Params(
        val userId: String,
        val groupId: String
    )

    override suspend fun invoke(params: Params) {
        val userId = params.userId
        val groupId = params.groupId

        val event = userNotificationsRemoteRepository.deleteNotification(userId, groupId)
        if (event is Event.Success) {
            withContext(Dispatchers.IO) {
                userNotificationsLocalRepository.deleteById(userId, groupId)
            }
        }
    }
}