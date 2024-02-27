package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.GroupApiService
import com.example.graduationproject.data.transormer.GroupTransformer
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.repository.GroupRepository

class GroupRepositoryImpl(private val groupApiService: GroupApiService) : GroupRepository {

    private val groupTransformer = GroupTransformer()
    override suspend fun fetchUserGroup(groupId: Long): Group {
        val response = groupApiService.fetchAllUserGroups(groupId)
        return groupTransformer.fromResponse(response)
    }
}