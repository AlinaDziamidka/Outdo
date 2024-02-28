package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.UserAwardApiService
import com.example.graduationproject.data.transormer.UserAwardTransformer
import com.example.graduationproject.domain.entity.UserAward
import com.example.graduationproject.domain.repository.UserAwardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserAwardRepositoryImpl(private val userAwardApiService: UserAwardApiService) :
    UserAwardRepository {


    private val userAwardTransformer = UserAwardTransformer()
    override suspend fun fetchAllAwardsByUserId(userId: Long): Flow<List<UserAward>> = flow {
        val response = userAwardApiService.fetchAllAwardsByUserId(userId)
        emit(response)
    }.map { responses ->
        responses.map { response ->
            userAwardTransformer.fromResponse(response)
        }
    }
}