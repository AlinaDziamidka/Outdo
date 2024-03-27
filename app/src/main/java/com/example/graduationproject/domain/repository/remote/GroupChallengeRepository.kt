package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.GroupChallenge
import kotlinx.coroutines.flow.Flow

interface GroupChallengeRepository {
    suspend fun fetchAllChallengesByGroupId(groupIdQuery: String): Flow<List<GroupChallenge>>
}