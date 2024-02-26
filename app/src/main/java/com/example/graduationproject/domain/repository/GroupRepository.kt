package com.example.graduationproject.domain.repository

import com.example.graduationproject.domain.entity.Group
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    suspend fun fetchAllUserGroups(groupId: Long): Flow<List<Group>>
}