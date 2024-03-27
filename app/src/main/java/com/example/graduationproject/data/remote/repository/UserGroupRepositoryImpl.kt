package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.UserGroupApiService
import com.example.graduationproject.data.remote.transormer.UserGroupTransformer
import com.example.graduationproject.data.remote.transormer.UserTransformer
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.remote.UserGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserGroupRepositoryImpl(private val userGroupApiService: UserGroupApiService) :
    UserGroupRepository {

    override suspend fun fetchAllGroupsByUserId(userIdQuery: String): Flow<List<UserGroup>> = flow {
        val query = "userId=\'$userIdQuery\'"
        val response = userGroupApiService.fetchAllGroupsByUserId(query)
        emit(response)
    }.map { responses ->
        responses.map { userGroupResponse ->
            val userGroupTransformer = UserGroupTransformer()
            userGroupTransformer.fromResponse(userGroupResponse)
        }
    }
}
