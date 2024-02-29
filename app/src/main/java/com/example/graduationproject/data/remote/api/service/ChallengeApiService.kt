package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.ChallengeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ChallengeApiService {
    @GET("challenges/{challenge_id}")
    suspend fun fetchChallenge(@Path("challenge_id") challengeId: Long): ChallengeResponse
}