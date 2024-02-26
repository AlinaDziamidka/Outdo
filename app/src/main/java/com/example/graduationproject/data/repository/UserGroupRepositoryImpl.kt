package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.group.GroupApiService
import com.example.graduationproject.data.remote.api.group.UserGroupApiService
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.GroupRepository
import com.example.graduationproject.domain.repository.UserGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserGroupRepositoryImpl(
    private val userGroupApiService: UserGroupApiService,
    private val groupApiService: GroupApiService
) : UserGroupRepository, GroupRepository {


    override suspend fun fetchAllUserGroupsId(userId: Long): Flow<List<UserGroup>> = flow {
        val response = userGroupApiService.fetchAllUserGroupsId(userId)
        emit(response)
    }.map { responses ->
        responses.map { response ->
            UserGroup(userId)
        }
    }

    override suspend fun fetchAllUserGroups(groupId: Long): Flow<List<Group>> = flow {
        val response = groupApiService.fetchAllUserGroups(groupId)
        emit(response)
    }.map { responses ->
        responses.map { response ->
            Group(
                groupId = groupId,
                groupName = response.groupName,
                creatorId = response.creatorId,
                groupAvatarPath = response.groupAvatarPath
            )
        }
    }
}