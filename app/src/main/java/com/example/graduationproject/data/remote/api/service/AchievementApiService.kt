package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.AchievementResponse
import com.example.graduationproject.data.remote.api.response.ChallengeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AchievementApiService {
    @GET("data/achievements")
    suspend fun fetchAchievementById(@Query("where") achievementIdQuery: String): Response<List<AchievementResponse>>

    @GET("data/achievements")
    suspend fun fetchDailyAchievement(@Query("where") achievementTypeQuery: String): Response<List<AchievementResponse>>
}