package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.GroupApiService
import com.example.graduationproject.data.remote.transormer.GroupTransformer
import com.example.graduationproject.data.remote.transormer.UserGroupTransformer
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.remote.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GroupRepositoryImpl(private val groupApiService: GroupApiService) : GroupRepository {

    override suspend fun fetchGroupsByGroupId(groupIdQuery: String): List<Group> {
        val query = "objectId=\'$groupIdQuery\'"
        val response = groupApiService.fetchGroupsByGroupId(query)
        return response.map { groupResponse ->
            val groupTransformer = GroupTransformer()
            groupTransformer.fromResponse(groupResponse)
        }
    }
}