package com.example.graduationproject.domain.repository

import com.example.graduationproject.domain.entity.UserCompetition
import kotlinx.coroutines.flow.Flow

interface UserCompetitionRepository {
    suspend fun fetchAllCompetitionsByUserId(userId: Long): Flow<List<UserCompetition>>
}