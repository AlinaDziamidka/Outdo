package com.example.graduationproject.domain.repository

import com.example.graduationproject.domain.entity.Session
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.util.Event
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun signIn(userIdentity: String, password: String): Event<Session>

    suspend fun signUp(userIdentity: String, password: String, username: String): Event<Session>

    suspend fun fetchToken(): Flow<String?>

    suspend fun saveToken(token: String)

    suspend fun saveUserProfile(userProfile: UserProfile)

}

