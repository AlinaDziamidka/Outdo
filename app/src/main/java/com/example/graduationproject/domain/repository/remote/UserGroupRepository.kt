package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.UserGroup
import retrofit2.Response

interface UserGroupRepository {
    suspend fun fetchAllGroupsByUserId(userIdQuery: String): Response<List<UserGroup>>
}