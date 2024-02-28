package com.example.graduationproject.domain.repository
import com.example.graduationproject.domain.entity.Competition

interface CompetitionRepository {
    suspend fun fetchUserCompetition(competitionId: Long): Competition
}