package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.service.AwardApiService
import com.example.graduationproject.data.transormer.AwardTransformer
import com.example.graduationproject.domain.entity.Award
import com.example.graduationproject.domain.repository.AwardRepository

class AwardRepositoryImpl(private val awardApiService: AwardApiService) : AwardRepository {

    override suspend fun fetchUserAward(awardId: Long): Award {
        val response = awardApiService.fetchUserAward(awardId)
        val awardTransformer = AwardTransformer()
        return awardTransformer.fromResponse(response)
    }
}