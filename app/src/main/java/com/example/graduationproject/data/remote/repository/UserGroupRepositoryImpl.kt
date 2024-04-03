package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.UserGroupApiService
import com.example.graduationproject.data.remote.transormer.UserGroupTransformer
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.remote.UserGroupRepository
import com.example.graduationproject.domain.util.Event
import doCall

class UserGroupRepositoryImpl(private val userGroupApiService: UserGroupApiService) :
    UserGroupRepository {

    override suspend fun fetchAllGroupsByUserId(userIdQuery: String): Event<List<UserGroup>> {
        val query = "userId=\'$userIdQuery\'"
        val event = doCall {
            return@doCall userGroupApiService.fetchAllGroupsByUserId(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val userGroups = response.map { userGroupResponse ->
                    val userGroupTransformer = UserGroupTransformer()
                    userGroupTransformer.fromResponse(userGroupResponse)
                }
                Event.Success(userGroups)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}