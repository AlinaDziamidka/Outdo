package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.request.UserGroupsRequest
import com.example.graduationproject.data.remote.api.response.UserGroupsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserGroupApiService {

    @GET("data/userGroups")
    suspend fun fetchAllGroupsByUserId(@Query("where") userIdQuery: String): Response<List<UserGroupsResponse>>

    @GET("data/userGroups")
    suspend fun fetchAllUsersByGroupId(@Query("where") groupIdQuery: String): Response<List<UserGroupsResponse>>

    @POST("data/userGroups")
    suspend fun insertUserGroup(@Body request: UserGroupsRequest): Response<UserGroupsResponse>

    @DELETE("data/bulk/userGroups")
    suspend fun deleteUserGroup(
        @Query("where") query: String,
    ): Response<Long>
}