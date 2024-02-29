package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.CompetitionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CompetitionApiService {
    @GET("competitions/{competition_id}")
    suspend fun fetchUserCompetition(@Path("competition_id") competitionId: Long): CompetitionResponse
}