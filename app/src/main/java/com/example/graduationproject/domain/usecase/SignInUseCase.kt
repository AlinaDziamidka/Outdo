package com.example.graduationproject.domain.usecase

import com.example.graduationproject.Event
import com.example.graduationproject.domain.entity.Session
import com.example.graduationproject.domain.repository.SessionRepository
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow

class SignInUseCase(private val sessionRepository: SessionRepository) :
    UseCase<SignInUseCase.Params, Flow<Session>> {

    data class Params(
        val identityValue: String,
        val password: String,
    )

    override suspend operator fun invoke(params: Params): Event<Flow<Session>> {
        val identityValue = params.identityValue
        val password = params.password
        return if (identityValue.isNotEmpty() && password.isNotEmpty()) {
            val session =
                sessionRepository.signIn(identityValue = identityValue, password = password)
            Event.Success(session)
        } else {
            Event.Failure("Email or password is wrong.")
        }
    }
}

object UserManager {
    private var userId: Long? = null

    fun getUserId(): Long? {
        return userId
    }

    fun setUserId(id: Long) {
        userId = id
    }
}