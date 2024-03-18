package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {
    @GET("users")
    suspend fun fetchUsersByUsername(@Query("username") usernameQuery: String): List<UserResponse>
}