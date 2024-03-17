package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.UsernameResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UsernameApiService {
    @GET("users/{username}")
    suspend fun fetchUsername(@Path("username") username: String): UsernameResponse
}