package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.util.Event
import retrofit2.Response

interface UserGroupRepository {
    suspend fun fetchAllGroupsByUserId(userIdQuery: String): Event<List<UserGroup>>
}