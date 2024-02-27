package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.UserGroupApiService
import com.example.graduationproject.data.transormer.UserGroupTransformer
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.UserGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserGroupRepositoryImpl(
    private val userGroupApiService: UserGroupApiService
) : UserGroupRepository {

    private val userGroupTransformer = UserGroupTransformer()

    override suspend fun fetchAllGroupsByUserId(userId: Long): Flow<List<UserGroup>> = flow {
        val response = userGroupApiService.fetchAllUserGroupsId(userId)
        emit(response)
    }.map { responses ->
        responses.map { response ->
            userGroupTransformer.fromResponse(response)
        }
    }
}