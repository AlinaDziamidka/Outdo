package com.example.graduationproject.domain.repository.remote
import com.example.graduationproject.domain.entity.Competition

interface CompetitionRepository {
    suspend fun fetchUserCompetition(competitionId: Long): Competition
}