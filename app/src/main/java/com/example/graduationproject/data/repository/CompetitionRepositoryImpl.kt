package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.service.CompetitionApiService
import com.example.graduationproject.data.transormer.CompetitionTransformer
import com.example.graduationproject.domain.entity.Competition
import com.example.graduationproject.domain.repository.CompetitionRepository

class CompetitionRepositoryImpl(private val competitionApiService: CompetitionApiService) :
    CompetitionRepository {

    override suspend fun fetchUserCompetition(competitionId: Long): Competition {
        val response = competitionApiService.fetchUserCompetition(competitionId)
        val competitionTransformer = CompetitionTransformer()
        return competitionTransformer.fromResponse(response)
    }
}