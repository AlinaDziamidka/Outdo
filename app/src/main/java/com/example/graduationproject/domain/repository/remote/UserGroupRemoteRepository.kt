package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.data.remote.api.request.UserGroupsRequest
import com.example.graduationproject.data.remote.api.response.UserGroupsResponse
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.util.Event
import retrofit2.Response
import retrofit2.http.Body

interface UserGroupRemoteRepository {
    suspend fun fetchAllGroupsByUserId(userIdQuery: String): Event<List<UserGroup>>

    suspend fun fetchAllUsersByGroupId(groupIdQuery: String): Event<List<UserGroup>>

    suspend fun insertUserGroup(userId: String, groupId: String): Event<UserGroup>

    suspend fun deleteUserGroup(userIdQuery: String, groupIdQuery: String): Event<Long>
}