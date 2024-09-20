package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.data.remote.api.response.UserAchievementResponse
import com.example.graduationproject.domain.entity.UserAchievement
import com.example.graduationproject.domain.util.Event
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserAchievementRemoteRepository {

    suspend fun fetchUserAchievementByUserId(userIdQuery: String): Event<List<UserAchievement>>

    suspend fun fetchUsersByAchievementId(achievementIdQuery: String): Event<List<UserAchievement>>

    suspend fun fetchByUserIdAndAchievementId(
        achievementIdQuery: String,
        userIdQuery: String
    ): Event<UserAchievement>

    suspend fun insertUserAchievements(
        userId: String,
        achievementId: String
    ): Event<UserAchievement>

    suspend fun updatePhoto(
        userIdQuery: String,
        achievementIdQuery: String,
        photoUrl: String
    ): Event<Long>
}