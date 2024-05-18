package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.util.Event

interface GroupChallengeRemoteRepository {
    suspend fun fetchAllChallengesByGroupId(groupIdQuery: String): Event<List<Challenge>>
}