package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.data.remote.api.request.SignUpRequest
import com.example.graduationproject.data.remote.api.response.SignUpResponse
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.util.Event
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChallengeRemoteRepository {
    suspend fun fetchChallengesById(challengeIdQuery: String): Event<Challenge>

    suspend fun fetchWeekChallenge(challengeTypeQuery: String): Event<Challenge>

    suspend fun fetchChallengesByStatusAndId(
        challengeIdQuery: String,
        challengeStatusQuery: String
    ): Event<Challenge>
}