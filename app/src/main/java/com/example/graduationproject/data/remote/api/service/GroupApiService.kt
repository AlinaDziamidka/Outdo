package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.request.GroupsRequest
import com.example.graduationproject.data.remote.api.response.GroupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GroupApiService {
    @GET("data/groups")
    suspend fun fetchGroupByGroupId(@Query("where") groupIdQuery: String): Response<List<GroupResponse>>

    @POST("data/groups")
    suspend fun insertGroup(@Body request: GroupsRequest): Response<GroupResponse>
}