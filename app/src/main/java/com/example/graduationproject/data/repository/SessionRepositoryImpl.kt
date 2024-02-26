package com.example.graduationproject.data.repository

import com.example.graduationproject.domain.entity.Session
import com.example.graduationproject.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

class SessionRepositoryImpl : SessionRepository {
    override suspend fun signIn(email: String, password: String): Flow<Session> {
        TODO("Not yet implemented")
    }
}