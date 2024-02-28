package com.example.graduationproject.data.remote.api

import com.example.graduationproject.data.remote.api.response.UserCompetitionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserCompetitionApiService {
    @GET("user_competitions/{user_id}")
    suspend fun fetchAllCompetitionsByUserId(@Path("user_id")userId: Long): List<UserCompetitionResponse>
}