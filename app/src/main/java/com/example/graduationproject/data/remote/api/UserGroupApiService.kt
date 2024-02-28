package com.example.graduationproject.data.remote.api

import com.example.graduationproject.data.remote.api.response.UserGroupsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserGroupApiService {
    @GET("user_groups/{user_id}")
    suspend fun fetchAllGroupsByUserId(@Path("user_id")userId: Long): List<UserGroupsResponse>
}