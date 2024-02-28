package com.example.graduationproject.domain.repository

import com.example.graduationproject.domain.entity.GroupChallenge
import kotlinx.coroutines.flow.Flow

interface GroupChallengeRepository {
    suspend fun fetchAllChallengesByGroupId(groupId: Long): Flow<List<GroupChallenge>>
}