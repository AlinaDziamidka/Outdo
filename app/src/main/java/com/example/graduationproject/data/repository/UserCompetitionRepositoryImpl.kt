package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.UserCompetitionApiService
import com.example.graduationproject.data.transormer.UserCompetitionTransformer
import com.example.graduationproject.domain.entity.UserCompetition
import com.example.graduationproject.domain.repository.UserCompetitionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserCompetitionRepositoryImpl(private val userCompetitionApiService: UserCompetitionApiService) :
    UserCompetitionRepository {

    private val userCompetitionTransformer = UserCompetitionTransformer()

    override suspend fun fetchAllCompetitionsByUserId(userId: Long): Flow<List<UserCompetition>> =
        flow {
            val response = userCompetitionApiService.fetchAllCompetitionsByUserId(userId)
            emit(response)
        }.map { responses ->
            responses.map { response ->
                userCompetitionTransformer.fromResponse(response)
            }
        }
}