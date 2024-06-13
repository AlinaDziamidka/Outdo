package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.UserAchievementResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserAchievementApiService {

    @GET("data/userAchievements")
    suspend fun fetchAchievementsByUserId(@Query("where") userIdQuery: String): Response<List<UserAchievementResponse>>

    @GET("data/userAchievements")
    suspend fun fetchUsersByAchievementId(@Query("where") achievementIdQuery: String): Response<List<UserAchievementResponse>>
}