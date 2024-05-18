package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.UserGroupsResponse
import com.example.graduationproject.data.remote.api.response.UserResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserGroupApiService {
    @GET("data/userGroups")
    suspend fun fetchAllGroupsByUserId(@Query("where") userIdQuery: String): Response<List<UserGroupsResponse>>

    @GET("data/userGroups")
    suspend fun fetchAllUsersByGroupId(@Query("where") groupIdQuery: String): Response<List<UserGroupsResponse>>
}