package com.example.graduationproject.data.remote.api

import com.example.graduationproject.data.remote.api.response.AwardResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AwardApiService {
    @GET("awards/{award_id}")
    suspend fun fetchUserAward(@Path("award_id") awardId: Long): AwardResponse
}