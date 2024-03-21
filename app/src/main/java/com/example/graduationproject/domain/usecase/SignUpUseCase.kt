package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.entity.Session
import com.example.graduationproject.domain.repository.SessionRepository
import com.example.graduationproject.domain.repository.UserRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.EventDomain
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

typealias SignUpUseCaseEvent = EventDomain<Session, SignUpUseCase.SignUpFailure>

class SignUpUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) :
    UseCase<SignUpUseCase.Params, SignUpUseCaseEvent> {

    data class Params(
        val userIdentity: String,
        val password: String,
        val username: String,
        val confirmPassword: String
    )

    sealed class SignUpFailure {
        data object UsernameExistFailure : SignUpFailure()
        data object UserIdentityExistFailure : SignUpFailure()
        data object UserIdentityFailure : SignUpFailure()
        data object PasswordFailure : SignUpFailure()
        data object ConfirmPasswordFailure : SignUpFailure()

        data object EmptyUsernameFailure : SignUpFailure()
        data object EmptyIdentityFailure : SignUpFailure()

        data object EmptyConfirmPasswordFailure : SignUpFailure()
    }

    override suspend operator fun invoke(params: Params): Flow<SignUpUseCaseEvent> = flow {
        val userIdentity = params.userIdentity
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
                        Log.d("signUp", "Failure event: ${event.exception}")
                        throw Exception(event.exception)
                    }
                }
            }
        }
    }

    private suspend fun validate(params: Params): SignUpFailure? {
        val username = params.username
        val usernameResponse = userRepository.fetchUsersByUsername(username)

        val userIdentity = params.userIdentity
        val identityResponse = userRepository.fetchUsersByIdentity(userIdentity)

        return when {
            usernameResponse.isNotEmpty() -> SignUpFailure.UsernameExistFailure
            params.userIdentity.isEmpty() -> SignUpFailure.EmptyIdentityFailure
            identityResponse.isNotEmpty() -> SignUpFailure.UserIdentityExistFailure
            !isValidEmail(params.userIdentity) -> SignUpFailure.UserIdentityFailure
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

