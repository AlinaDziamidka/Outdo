package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.GroupApiService
import com.example.graduationproject.data.remote.transormer.GroupTransformer
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.repository.remote.GroupRepository
import com.example.graduationproject.domain.util.Event
import doCall
class GroupRepositoryImpl(private val groupApiService: GroupApiService) : GroupRepository {

    override suspend fun fetchGroupsByGroupId(groupIdQuery: String): Event<Group> {
        val query = "objectId=\'$groupIdQuery\'"
        val event = doCall {
            return@doCall groupApiService.fetchGroupByGroupId(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val groupTransformer = GroupTransformer()
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
