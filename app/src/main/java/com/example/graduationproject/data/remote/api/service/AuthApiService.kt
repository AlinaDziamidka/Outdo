package com.example.graduationproject.data.remote.api.service


import android.util.Log
import com.example.graduationproject.data.remote.api.request.SignInRequest
import com.example.graduationproject.data.remote.api.request.SignUpRequest
import com.example.graduationproject.data.remote.api.response.SignInResponse
import com.example.graduationproject.data.remote.api.response.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("users/login")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    @POST("users/register")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>
}