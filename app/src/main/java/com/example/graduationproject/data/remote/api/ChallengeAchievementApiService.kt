package com.example.graduationproject.data.remote.api

import com.example.graduationproject.data.remote.api.response.ChallengeAchievementResponse

import retrofit2.http.GET
import retrofit2.http.Path

interface ChallengeAchievementApiService {

    @GET("challenge_achievement/{challenge_id}")
    suspend fun fetchAllAchievementsByChallengeId(@Path("challenge_id") challengeId: Long):
            List<ChallengeAchievementResponse>
}