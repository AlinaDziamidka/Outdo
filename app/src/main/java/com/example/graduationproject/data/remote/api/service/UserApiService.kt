package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.request.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("users")
    suspend fun createUser(@Body user: UserRequest): Response<User>
}