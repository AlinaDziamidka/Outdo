package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.entity.Session
import com.example.graduationproject.domain.repository.remote.SessionRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val sessionRepository: SessionRepository) :
    UseCase<SignInUseCase.Params, Session> {

    data class Params(
        val userIdentity: String,
        val password: String,
    )

    override suspend operator fun invoke(params: Params): Flow<Session> = flow {
        val userIdentity = params.userIdentity
        val password = params.password
        if (userIdentity.isNotEmpty() && password.isNotEmpty()) {
            val event = sessionRepository.signIn(userIdentity = userIdentity, password = password)
            when (event) {
                is Event.Success -> {
                    val session = event.data
                    sessionRepository.saveToken(session.token)
                    sessionRepository.saveUserProfile(session.userProfile)
                    emit(session)
                }

                is Event.Failure -> {
                    throw Exception(event.exception)
                }
            }
        } else {
            throw Exception("Email or password is wrong.")
        }
    }
}
