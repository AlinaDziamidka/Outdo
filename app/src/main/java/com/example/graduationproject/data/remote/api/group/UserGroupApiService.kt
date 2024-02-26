package com.example.graduationproject.data.remote.api.group

import com.example.graduationproject.data.remote.api.group.response.UserGroupsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserGroupApiService {
    @GET("groups/{user_id}")
    suspend fun fetchAllUserGroupsId(@Path("user_id")userId: Long): List<UserGroupsResponse>
}