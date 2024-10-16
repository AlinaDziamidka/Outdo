package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.entity.AchievementStatus
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.entity.UserAchievement
import com.example.graduationproject.domain.repository.local.UserAchievementLocalRepository
import com.example.graduationproject.domain.repository.remote.PhotoUploadRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserAchievementRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class PhotoUploadUseCase @Inject constructor(
    private val photoUploadRemoteRepository: PhotoUploadRemoteRepository,
    private val userAchievementRemoteRepository: UserAchievementRemoteRepository,
    private val userAchievementLocalRepository: UserAchievementLocalRepository
) : UseCase<PhotoUploadUseCase.Params, String> {

    data class Params(
        val userId: String,
        val achievementId: String,
        val photo: File,
    )

    override suspend fun invoke(params: Params): Flow<String> = flow {
        val userId = params.userId
        val achievementId = params.achievementId
        val photo = params.photo
        val event = photoUploadRemoteRepository.uploadFile(photo)

        when (event) {
            is Event.Success -> {
                val url = event.data
                val updatePhotoEvent = updateUserAchievementPhoto(userId, achievementId, url)
                if (updatePhotoEvent is Event.Success) {
                    val userAchievement = UserAchievement(
                        userId = userId,
                        achievementId = achievementId,
                        achievementStatus = AchievementStatus.COMPLETED,
                        achievementType = AchievementType.CHALLENGE,
                        photoUrl = photo.toString()
                    )
                    withContext(Dispatchers.IO) {
                        userAchievementLocalRepository.insertOne(userAchievement)
                    }
                    emit(url)
                } else {
                    throw Exception(updatePhotoEvent.toString())
                }
            }

            is Event.Failure -> {
                throw Exception(event.exception)
            }
        }
    }

    private suspend fun updateUserAchievementPhoto(
        userId: String, achievementId: String, url: String
    ): Event<Long> {

        val userAchievementEvent =
            userAchievementRemoteRepository.updatePhoto(userId, achievementId, url)

        return when (userAchievementEvent) {
            is Event.Success -> {
                Event.Success(userAchievementEvent.data)
            }

            is Event.Failure -> {
                throw Exception(userAchievementEvent.exception)
            }
        }
    }
}
