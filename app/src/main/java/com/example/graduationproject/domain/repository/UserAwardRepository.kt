package com.example.graduationproject.domain.repository

import com.example.graduationproject.domain.entity.UserAward
import kotlinx.coroutines.flow.Flow

interface UserAwardRepository {
    suspend fun fetchAllAwardsByUserId(userId: Long): Flow<List<UserAward>>
}