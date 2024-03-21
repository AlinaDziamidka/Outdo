package com.example.graduationproject.data.remote.api.service

import android.provider.ContactsContract.CommonDataKinds.Identity
import com.example.graduationproject.data.remote.api.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {
    @GET("data/users")
    suspend fun fetchUsersByUsername(@Query("where") usernameQuery: String): List<UserResponse>

    @GET("data/users")
    suspend fun fetchUsersByIdentity(@Query("where") userIdentityQuery: String): List<UserResponse>
}