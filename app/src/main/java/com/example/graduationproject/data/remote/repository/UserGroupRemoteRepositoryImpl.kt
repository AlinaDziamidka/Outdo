package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.request.UserGroupsRequest
import com.example.graduationproject.data.remote.api.service.UserGroupApiService
import com.example.graduationproject.data.remote.transormer.UserGroupTransformer
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class UserGroupRemoteRepositoryImpl @Inject constructor(private val userGroupApiService: UserGroupApiService) :
    UserGroupRemoteRepository {

    private val userGroupTransformer = UserGroupTransformer()

    override suspend fun fetchAllGroupsByUserId(userIdQuery: String): Event<List<UserGroup>> {
        val query = "userId=\'$userIdQuery\'"
        val event = doCall {
            return@doCall userGroupApiService.fetchAllGroupsByUserId(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val userGroups = response.map { userGroupResponse ->
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

    override suspend fun fetchAllUsersByGroupId(groupIdQuery: String): Event<List<UserGroup>> {
        val query = "groupId=\'$groupIdQuery\'"
        val event = doCall {
            return@doCall userGroupApiService.fetchAllUsersByGroupId(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val userGroups = response.map { userGroupResponse ->
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

    override suspend fun insertUserGroup(userId: String, groupId: String): Event<UserGroup> {
        val event = doCall {
            val request = UserGroupsRequest(userId, groupId)
            return@doCall userGroupApiService.insertUserGroup(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val userGroup = userGroupTransformer.fromResponse(response)
                Event.Success(userGroup)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun deleteUserGroup(userIdQuery: String, groupIdQuery: String): Event<Long> {
        val query = "userId='$userIdQuery' AND groupId='$groupIdQuery'"
        val event = doCall {
            return@doCall userGroupApiService.deleteUserGroup(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                Event.Success(response)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}
