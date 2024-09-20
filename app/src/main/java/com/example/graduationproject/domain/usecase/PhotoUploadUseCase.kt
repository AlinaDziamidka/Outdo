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

    override suspend fun invoke(params: Params): Flow<String> =
        flow {
            val userId = params.userId
            val achievementId = params.achievementId
            val photo = params.photo
            val event = photoUploadRemoteRepository.uploadFile(photo)

            when (event) {
                is Event.Success -> {
                    val url = event.data
                    Log.d("PhotoUploadUseCase", "Upload photo url $url")
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
                        Log.e(
                            "PhotoUploadUseCase",
                            "Failed2 to insert photo to userAchievement: ${updatePhotoEvent.toString()}"
                        )
                        throw Exception(updatePhotoEvent.toString())
                    }
                }

                is Event.Failure -> {
                    Log.e("PhotoUploadUseCase", "Failed to upload photo: ${event.exception}")
                    throw Exception(event.exception)
                }
            }
        }

    private suspend fun updateUserAchievementPhoto(
        userId: String,
        achievementId: String,
        url: String
    ): Event<Long> {

        Log.d("PhotoUploadUseCase", "Start to update photo $url")
        val userAchievementEvent =
            userAchievementRemoteRepository.updatePhoto(userId, achievementId, url)

        return when (userAchievementEvent) {
            is Event.Success -> {
                Log.e("PhotoUploadUseCase", "Success to update photo: ${userAchievementEvent.data}")
                Event.Success(userAchievementEvent.data)
            }

            is Event.Failure -> {
                Log.e(
                    "PhotoUploadUseCase",
                    "Failed1 to insert photo to userAchievement: ${userAchievementEvent.exception}"
                )
                throw Exception(userAchievementEvent.exception)
            }
        }
    }
}
