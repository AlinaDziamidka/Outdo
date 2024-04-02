package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.request.SignInRequest
import com.example.graduationproject.data.remote.api.request.SignUpRequest
import com.example.graduationproject.data.remote.api.service.AuthApiService
import com.example.graduationproject.data.remote.prefs.PrefsDataSource
import com.example.graduationproject.domain.entity.Session
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.remote.SessionRepository
import com.example.graduationproject.domain.util.Event
import doCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor (
    private val prefsDataSource: PrefsDataSource,
    private val authApiService: AuthApiService,
) : SessionRepository {
    override suspend fun signIn(userIdentity: String, password: String): Event<Session> {
        val event = doCall {
            val request = SignInRequest(userIdentity, password)
            return@doCall authApiService.signIn(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val session = Session(
                    token = response.token,
                    userProfile = UserProfile(
                        userId = response.userId,
                        userIdentity = response.userIdentity,
                        username = response.username,
                        userAvatarPath = response.userAvatarPath
                    )
                )
                Event.Success(session)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun signUp(
        userIdentity: String, password: String, username: String
    ): Event<Session> {
        val event = doCall {
            val request = SignUpRequest(userIdentity = userIdentity, password = password, username = username)
            return@doCall authApiService.signUp(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val session = Session(
                    token = "",
                    userProfile = UserProfile(
                        userId = response.userId,
                        userIdentity = response.userIdentity,
                        username = response.username ?: "",
                        userAvatarPath = ""
                    )
                )
                Event.Success(session)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun fetchToken(): Flow<String?> = flow {
        val token = prefsDataSource.fetchToken()
        emit(token)
    }

    override suspend fun saveToken(token: String) = prefsDataSource.saveToken(token)

    override suspend fun saveUserProfile(userProfile: UserProfile) =
        prefsDataSource.saveUserProfile(userProfile)
}