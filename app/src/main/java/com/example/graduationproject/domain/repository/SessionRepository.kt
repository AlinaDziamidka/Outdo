package com.example.graduationproject.domain.repository

import com.example.graduationproject.domain.entity.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun signIn(identityValue: String, password: String): Flow<Session>
}