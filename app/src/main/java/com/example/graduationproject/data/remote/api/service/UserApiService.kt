package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {
    @GET("data/users")
    suspend fun fetchUsersByUsername(@Query("where") usernameQuery: String): List<UserResponse>

    @GET("data/users")
    suspend fun fetchUsersByEmail(@Query("where") userEmailQuery: String): List<UserResponse>

    @GET("data/users")
    suspend fun fetchUsersById(@Query("where") userIdQuery: String): Response<List<UserResponse>>
}