package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.GroupChallenge
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface GroupChallengeRepository {
    suspend fun fetchAllChallengesByGroupId(groupIdQuery: String): Response<List<GroupChallenge>>
}