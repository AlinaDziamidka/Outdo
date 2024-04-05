package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.ChallengeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ChallengeApiService {
    @GET("data/challenges")
    suspend fun fetchChallengesById(@Query("where") challengeIdQuery: String): Response<List<ChallengeResponse>>
}