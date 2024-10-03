package com.example.graduationproject.domain.usecase

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.util.LoadManager
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchNotificationsUseCase @Inject constructor(
    @Remote private val remoteLoadManager: LoadManager,
    @Local private val localLoadManager: LoadManager
) : UseCase<FetchNotificationsUseCase.Params, MutableList<Pair<UserProfile, Group>>> {

    data class Params(
        val userId: String
    )

    override suspend fun invoke(params: Params): Flow<MutableList<Pair<UserProfile, Group>>> =
        flow {
            val userId = params.userId
            val localNotifications = localLoadManager.fetchNotificationsByUserId(userId)
            emit(localNotifications.toMutableList())

            val remoteNotifications = remoteLoadManager.fetchNotificationsByUserId(userId)
                emit(remoteNotifications.toMutableList())
        }
}