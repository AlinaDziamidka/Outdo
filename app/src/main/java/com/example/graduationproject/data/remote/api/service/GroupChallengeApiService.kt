package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.GroupChallengeResponse
import com.example.graduationproject.data.remote.api.response.GroupResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupChallengeApiService {

    @GET("data/groupChallenge")
    suspend fun fetchAllChallengesByGroupId(@Query("where") groupIdQuery: String): List<GroupChallengeResponse>
}