package com.example.graduationproject.data.remote.api

import com.example.graduationproject.data.remote.api.response.UserAwardResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserAwardApiService {
    @GET("user_awards/{user_id}")
    suspend fun fetchAllAwardsByUserId(@Path("user_id") userId: Long): List<UserAwardResponse>
}