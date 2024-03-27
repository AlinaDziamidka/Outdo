package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.CompetitionApiService
import com.example.graduationproject.data.remote.transormer.CompetitionTransformer
import com.example.graduationproject.domain.entity.Competition
import com.example.graduationproject.domain.repository.remote.CompetitionRepository

class CompetitionRepositoryImpl(private val competitionApiService: CompetitionApiService) :
    CompetitionRepository {

    override suspend fun fetchUserCompetition(competitionId: Long): Competition {
        val response = competitionApiService.fetchUserCompetition(competitionId)
        val competitionTransformer = CompetitionTransformer()
        return competitionTransformer.fromResponse(response)
    }
}