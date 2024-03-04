package com.example.graduationproject.data.repository

import com.example.graduationproject.domain.entity.Session
import com.example.graduationproject.domain.repository.SessionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SessionRepositoryImpl : SessionRepository {
    override suspend fun signIn(identityValue: String, password: String): Flow<Session> = flow{
        delay(100)
        emit(Session("1a40edcf-7cef-4fd0-861e-5730f07a0504"))
    }
}