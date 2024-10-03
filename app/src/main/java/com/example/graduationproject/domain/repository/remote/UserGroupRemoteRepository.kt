package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.util.Event

interface UserGroupRemoteRepository {
    suspend fun fetchAllGroupsByUserId(userIdQuery: String): Event<List<UserGroup>>

    suspend fun fetchAllUsersByGroupId(groupIdQuery: String): Event<List<UserGroup>>

    suspend fun insertUserGroup(userId: String, groupId: String): Event<UserGroup>

    suspend fun deleteUserGroup(userIdQuery: String, groupIdQuery: String): Event<Long>
}