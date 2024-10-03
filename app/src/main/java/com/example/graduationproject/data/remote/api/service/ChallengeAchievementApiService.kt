package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.request.ChallengeAchievementRequest
import com.example.graduationproject.data.remote.api.response.ChallengeAchievementResponse
import retrofit2.Response
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChallengeAchievementApiService {

    @GET("data/challengeAchievements")
    suspend fun  fetchAllAchievementsByChallengeId(@Query("where") challengeId: String): Response<List<ChallengeAchievementResponse>>

    @POST("data/challengeAchievements")
    suspend fun insertChallengeAchievement(@Body request: ChallengeAchievementRequest): Response<ChallengeAchievementResponse>
}