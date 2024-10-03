package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.request.GroupsRequest
import com.example.graduationproject.data.remote.api.service.GroupApiService
import com.example.graduationproject.data.remote.transormer.GroupTransformer
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.repository.remote.GroupRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class GroupRemoteRepositoryImpl @Inject constructor(private val groupApiService: GroupApiService) : GroupRemoteRepository {

    private val groupTransformer = GroupTransformer()

    override suspend fun fetchGroupsByGroupId(groupIdQuery: String): Event<Group> {
        val query = "objectId=\'$groupIdQuery\'"
        val event = doCall {
            return@doCall groupApiService.fetchGroupByGroupId(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data.first()
                val group = groupTransformer.fromResponse(response)
                Event.Success(group)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun insertGroup(groupName: String, creatorId: String, groupAvatarPath: String?): Event<Group>{
        val event = doCall {
            val request = GroupsRequest(groupName, creatorId, groupAvatarPath)
            return@doCall groupApiService.insertGroup(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val group = groupTransformer.fromResponse(response)
                Event.Success(group)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}
