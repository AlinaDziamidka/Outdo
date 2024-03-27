package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.UserCompetitionApiService
import com.example.graduationproject.data.remote.transormer.UserCompetitionTransformer
import com.example.graduationproject.domain.entity.UserCompetition
import com.example.graduationproject.domain.repository.remote.UserCompetitionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserCompetitionRepositoryImpl(private val userCompetitionApiService: UserCompetitionApiService) :
    UserCompetitionRepository {

    override suspend fun fetchAllCompetitionsByUserId(userId: Long): Flow<List<UserCompetition>> =
        flow {
            val response = userCompetitionApiService.fetchAllCompetitionsByUserId(userId)
            emit(response)
        }.map { responses ->
            responses.map { response ->
                val userCompetitionTransformer = UserCompetitionTransformer()
                userCompetitionTransformer.fromResponse(response)
            }
        }
}