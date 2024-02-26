package com.example.graduationproject.domain.repository

import com.example.graduationproject.domain.entity.UserGroup
import kotlinx.coroutines.flow.Flow

interface UserGroupRepository {
    suspend fun fetchAllUserGroupsId(userId: Long): Flow<List<UserGroup>>
}