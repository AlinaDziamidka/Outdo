package com.example.graduationproject.domain.usecase

import com.example.graduationproject.domain.entity.Session
import com.example.graduationproject.domain.repository.remote.SessionRepository
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.EventDomain
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

typealias SignUpUseCaseEvent = EventDomain<Session, SignUpUseCase.SignUpFailure>

class SignUpUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRemoteRepository: UserRemoteRepository
) :
    UseCase<SignUpUseCase.Params, SignUpUseCaseEvent> {

    data class Params(
        val userEmail: String,
        val password: String,
        val username: String,
        val confirmPassword: String
    )

    sealed class SignUpFailure {
        data object UsernameExistFailure : SignUpFailure()
        data object UserEmailExistFailure : SignUpFailure()
        data object UserEmailFailure : SignUpFailure()
        data object PasswordFailure : SignUpFailure()
        data object ConfirmPasswordFailure : SignUpFailure()

        data object EmptyUsernameFailure : SignUpFailure()
        data object EmptyEmailFailure : SignUpFailure()

        data object EmptyConfirmPasswordFailure : SignUpFailure()
    }

    override suspend operator fun invoke(params: Params): Flow<SignUpUseCaseEvent> = flow {
        val userIdentity = params.userEmail
        val password = params.password
        val username = params.username

        when (val error = validate(params)) {
            is SignUpFailure -> emit(EventDomain.Failure(error))
            null -> {
                val event = sessionRepository.signUp(
                    userIdentity = userIdentity,
                    password = password,
                    username = username
                )

                when (event) {
                    is Event.Success -> {
                        val session = event.data
                        sessionRepository.saveToken(session.token)
                        sessionRepository.saveUserProfile(session.userProfile)
                        emit(EventDomain.Success(session))
                    }

                    is Event.Failure -> {
                        throw Exception(event.exception)
                    }
                }
            }
        }
    }

    private suspend fun validate(params: Params): SignUpFailure? {
        val username = params.username
        val usernameResponse = userRemoteRepository.fetchUsersByUsername(username)

        val userEmail = params.userEmail
        val emailResponse = userRemoteRepository.fetchUsersByEmail(userEmail)

        return when {
            usernameResponse.isNotEmpty() -> SignUpFailure.UsernameExistFailure
            params.userEmail.isEmpty() -> SignUpFailure.EmptyEmailFailure
            emailResponse.isNotEmpty() -> SignUpFailure.UserEmailExistFailure
            !isValidEmail(params.userEmail) -> SignUpFailure.UserEmailFailure
            params.confirmPassword.isEmpty() -> SignUpFailure.EmptyConfirmPasswordFailure
            !isValidPassword(params.password) -> SignUpFailure.PasswordFailure
            params.username.isEmpty() -> SignUpFailure.EmptyUsernameFailure
            params.password != params.confirmPassword -> SignUpFailure.ConfirmPasswordFailure
            else -> null
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val digitRegex = Regex("\\d")
        val lowerCaseRegex = Regex("[a-z]")
        val upperCaseRegex = Regex("[A-Z]")

        return password.length >= 8 &&
                digitRegex.containsMatchIn(password) &&
                lowerCaseRegex.containsMatchIn(password) &&
                upperCaseRegex.containsMatchIn(password)
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        return emailRegex.matches(email)
    }
}

