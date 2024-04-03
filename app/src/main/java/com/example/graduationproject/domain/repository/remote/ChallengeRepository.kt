package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.util.Event
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ChallengeRepository {
    suspend fun fetchChallengesById(challengeIdQuery: String): Event<Challenge>
}