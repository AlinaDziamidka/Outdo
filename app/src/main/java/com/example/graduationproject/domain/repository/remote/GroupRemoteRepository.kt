package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.util.Event

interface GroupRemoteRepository {
    suspend fun fetchGroupsByGroupId(groupIdQuery: String): Event<Group>
}