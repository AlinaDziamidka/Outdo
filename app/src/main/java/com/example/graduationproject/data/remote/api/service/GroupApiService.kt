package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.GroupResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GroupApiService {
    @GET("groups/{group_id}")
    suspend fun fetchUserGroup(@Path("group_id") groupId: Long): GroupResponse
}