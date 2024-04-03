package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.util.Event
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface GroupRepository {
    suspend fun fetchGroupsByGroupId(groupIdQuery: String): Event<Group>
}