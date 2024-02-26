package com.example.graduationproject.data.remote.api.group

import com.example.graduationproject.data.remote.api.group.response.GroupResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GroupApiService {
    @GET("user_groups/{group_id}")
    suspend fun fetchAllUserGroups(@Path("group_id") groupId: Long): List<GroupResponse>
}