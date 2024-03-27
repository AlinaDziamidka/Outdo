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

    override suspend fun fetchGroupsByGroupId(groupIdQuery: String): Flow<List<Group>> = flow {
        val query = "objectId=\'$groupIdQuery\'"
        val response = groupApiService.fetchGroupsByGroupId(query)
        emit(response)
    }.map { responses ->
        responses.map { groupResponse ->
            val groupTransformer = GroupTransformer()
            groupTransformer.fromResponse(groupResponse)
        }
    }
}