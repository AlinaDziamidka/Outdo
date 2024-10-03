package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.repository.local.UserAchievementLocalRepository
import com.example.graduationproject.domain.repository.remote.UserAchievementRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchFriendPhotoUseCase @Inject constructor(
    private val userAchievementRemoteRepository: UserAchievementRemoteRepository,
    private val userAchievementLocalRepository: UserAchievementLocalRepository
) : UseCase<FetchFriendPhotoUseCase.Params, String> {

    data class Params(
        val userId: String,
        val achievementId: String
    )

    override suspend fun invoke(params: Params): Flow<String> = flow {
        val userId = params.userId
        val achievementId = params.achievementId

        var friendPhotoUrl = withContext(Dispatchers.IO) {
            userAchievementLocalRepository.fetchByUserIdAndAchievementId(
                achievementId,
                userId
            )?.photoUrl
        }

        if (friendPhotoUrl == null) {
            friendPhotoUrl = getRemotePhotoUrl(achievementId, userId)
        }
        emit(friendPhotoUrl)
    }

    private suspend fun getRemotePhotoUrl(
        achievementId: String,
        userId: String
    ): String {

        val event =
            userAchievementRemoteRepository.fetchByUserIdAndAchievementId(achievementId, userId)

        return when (event) {
            is Event.Success -> {
                val userAchievement = event.data
                withContext(Dispatchers.IO) {
                    userAchievementLocalRepository.updateOne(userAchievement)
                }
                userAchievement.photoUrl.toString()
            }

            is Event.Failure -> {
                throw Exception(event.exception)
            }
        }
    }
}