package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.service.GroupApiService
import com.example.graduationproject.data.transormer.GroupTransformer
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.repository.GroupRepository

class GroupRepositoryImpl(private val groupApiService: GroupApiService) : GroupRepository {

    override suspend fun fetchUserGroup(groupId: Long): Group {
        val response = groupApiService.fetchUserGroup(groupId)
        val groupTransformer = GroupTransformer()
        return groupTransformer.fromResponse(response)
    }
}