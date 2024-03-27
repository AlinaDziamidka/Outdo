package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.UserAwardApiService
import com.example.graduationproject.data.remote.transormer.UserAwardTransformer
import com.example.graduationproject.domain.entity.UserAward
import com.example.graduationproject.domain.repository.remote.UserAwardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserAwardRepositoryImpl(private val userAwardApiService: UserAwardApiService) :
    UserAwardRepository {

    override suspend fun fetchAllAwardsByUserId(userId: Long): Flow<List<UserAward>> = flow {
        val response = userAwardApiService.fetchAllAwardsByUserId(userId)
        emit(response)
    }.map { responses ->
        responses.map { response ->
            val userAwardTransformer = UserAwardTransformer()
            userAwardTransformer.fromResponse(response)
        }
    }
}