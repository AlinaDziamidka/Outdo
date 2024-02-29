package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.service.UserGroupApiService
import com.example.graduationproject.data.transormer.UserGroupTransformer
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.UserGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserGroupRepositoryImpl(private val userGroupApiService: UserGroupApiService) :
    UserGroupRepository {

    override suspend fun fetchAllGroupsByUserId(userId: Long): Flow<List<UserGroup>> = flow {
        val response = userGroupApiService.fetchAllGroupsByUserId(userId)
        emit(response)
    }.map { responses ->
        responses.map { response ->
            val userGroupTransformer = UserGroupTransformer()
            userGroupTransformer.fromResponse(response)
        }
    }
}