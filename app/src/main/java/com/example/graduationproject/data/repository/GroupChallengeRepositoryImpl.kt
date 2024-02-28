package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.GroupChallengeApiService
import com.example.graduationproject.data.transormer.GroupChallengeTransformer
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.repository.GroupChallengeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GroupChallengeRepositoryImpl(private val groupChallengeApiService: GroupChallengeApiService) :
    GroupChallengeRepository {


    private val groupChallengeTransformer = GroupChallengeTransformer()

    override suspend fun fetchAllChallengesByGroupId(groupId: Long): Flow<List<GroupChallenge>> =
        flow {
            val response = groupChallengeApiService.fetchAllChallengesByGroupId(groupId)
            emit(response)
        }.map { responses ->
            responses.map { response ->
                groupChallengeTransformer.fromResponse(response)
            }
        }
}