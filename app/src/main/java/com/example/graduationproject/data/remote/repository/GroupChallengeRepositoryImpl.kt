package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.GroupChallengeApiService
import com.example.graduationproject.data.remote.transormer.GroupChallengeTransformer
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.repository.remote.GroupChallengeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GroupChallengeRepositoryImpl(private val groupChallengeApiService: GroupChallengeApiService) :
    GroupChallengeRepository {

    override suspend fun fetchAllChallengesByGroupId(groupIdQuery: String): Flow<List<GroupChallenge>> =
        flow {
            val query = "groupId=\'$groupIdQuery\'"
            val response = groupChallengeApiService.fetchAllChallengesByGroupId(query)
            emit(response)
        }.map { responses ->
            responses.map { groupChallengeResponse ->
                val groupChallengeTransformer = GroupChallengeTransformer()
                groupChallengeTransformer.fromResponse(groupChallengeResponse)
            }
        }
}
