package com.example.graduationproject.data.remote.api

import com.example.graduationproject.data.remote.api.response.AchievementResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AchievementApiService {
    @GET("achievements/{achievement_id}")
    suspend fun fetchAchievement(@Path("achievement_id") achievementId: Long): AchievementResponse
}