package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.GroupChallengeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupChallengeApiService {

    @GET("data/groupChallenge")
    suspend fun fetchAllChallengesByGroupId(@Query("where") groupIdQuery: String): Response<List<GroupChallengeResponse>>
}