package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.GroupChallengeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GroupChallengeApiService {

    @GET("group_challenge/{group_id}")
    suspend fun fetchAllChallengesByGroupId(@Path("group_id") groupId: Long): List<GroupChallengeResponse>
}