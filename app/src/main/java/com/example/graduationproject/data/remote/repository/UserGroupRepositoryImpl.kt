package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.UserGroupApiService
import com.example.graduationproject.data.remote.transormer.UserGroupTransformer
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.remote.UserGroupRepository

class UserGroupRepositoryImpl(private val userGroupApiService: UserGroupApiService) :
    UserGroupRepository {

    override suspend fun fetchAllGroupsByUserId(userIdQuery: String): List<UserGroup> {
        val query = "userId=\'$userIdQuery\'"
        val response = userGroupApiService.fetchAllGroupsByUserId(query)
        return response.map { userGroupResponse ->
            val userGroupTransformer = UserGroupTransformer()
            userGroupTransformer.fromResponse(userGroupResponse)
        }
    }
}