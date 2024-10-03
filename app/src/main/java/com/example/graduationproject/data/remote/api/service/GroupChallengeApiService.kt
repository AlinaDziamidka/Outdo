package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.request.GroupChallengeRequest
import com.example.graduationproject.data.remote.api.response.GroupChallengeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GroupChallengeApiService {

    @GET("data/groupChallenge")
    suspend fun fetchAllChallengesByGroupId(@Query("where") groupIdQuery: String): Response<List<GroupChallengeResponse>>

    @POST("data/groupChallenge")
    suspend fun insertGroupChallenge(@Body request: GroupChallengeRequest): Response<GroupChallengeResponse>
}