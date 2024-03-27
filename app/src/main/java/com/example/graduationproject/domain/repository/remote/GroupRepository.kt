package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Group
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    suspend fun fetchGroupsByGroupId(groupIdQuery: String): Flow<List<Group>>
}