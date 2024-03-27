package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.UserGroup
import kotlinx.coroutines.flow.Flow

interface UserGroupRepository {
    suspend fun fetchAllGroupsByUserId(userIdQuery: String): Flow<List<UserGroup>>
}