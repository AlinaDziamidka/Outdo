package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.request.UpdatePhotoRequest
import com.example.graduationproject.data.remote.api.request.UserAchievementRequest
import com.example.graduationproject.data.remote.api.response.AchievementResponse
import com.example.graduationproject.data.remote.api.response.UserAchievementResponse
import com.example.graduationproject.domain.entity.ChallengeAchievement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserAchievementApiService {

    @GET("data/userAchievements")
    suspend fun fetchAchievementsByUserId(@Query("where") userIdQuery: String): Response<List<UserAchievementResponse>>

    @GET("data/userAchievements")
    suspend fun fetchUsersByAchievementId(@Query("where") achievementIdQuery: String): Response<List<UserAchievementResponse>>

    @GET("data/userAchievements")
    suspend fun fetchByUserIdAndAchievementId(@Query("where") query: String): Response<List<UserAchievementResponse>>

    @POST("data/userAchievements")
    suspend fun insertUserAchievements(@Body request: UserAchievementRequest): Response<UserAchievementResponse>

    @PUT("data/bulk/UserAchievements")
    suspend fun updatePhoto(
        @Query("where") whereClause: String,
        @Body uploadPhotoRequest: UpdatePhotoRequest
    ): Response<Long>
}