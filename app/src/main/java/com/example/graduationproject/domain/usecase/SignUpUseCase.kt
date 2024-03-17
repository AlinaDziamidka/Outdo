package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.entity.Session
import com.example.graduationproject.domain.repository.SessionRepository
import com.example.graduationproject.domain.repository.UsernameRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val usernameRepository: UsernameRepository
) :
    UseCase<SignUpUseCase.Params, Session> {

    data class Params(
        val userIdentity: String,
        val password: String,
        val username: String
    )


    private fun passwordConditions(password: String): Boolean {
        val digitRegex = Regex("\\d")
        val lowerCaseRegex = Regex("[a-z]")
        val upperCaseRegex = Regex("[A-Z]")

        return password.length >= 8 &&
                digitRegex.containsMatchIn(password) &&
                lowerCaseRegex.containsMatchIn(password) &&
                upperCaseRegex.containsMatchIn(password)
    }

    private suspend fun handleException(
        userIdentity: String,
        password: String,
        username: String
    ): SignUpException {
        return try {
//            val usernameResponse = usernameRepository.fetchUsername(username)
//            if (usernameResponse.username != username) {
//                SignUpException.UsernameException
//            } else
                if (userIdentity.isEmpty()) {
                SignUpException.UserIdentityException
            } else if (!passwordConditions(password)) {
                SignUpException.PasswordException
            } else {
                SignUpException.NoException
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend operator fun invoke(params: Params): Flow<Session> = flow {
        val userIdentity = params.userIdentity
        val password = params.password
        val username = params.username

        try {
            val signUpException = handleException(userIdentity, password, username)
            Log.d("signUp", "SignUpException: $signUpException")
            if (signUpException == SignUpException.NoException) {
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
                        emit(session)
                    }

                    is Event.Failure -> {
                        Log.d("signUp", "Failure event: ${event.exception}")
                        throw Exception(event.exception)
                    }
                }
            } else {
                Log.d("signUp", "SignUpException is not NoException: $signUpException")
               throw signUpException
            }
        } catch (e: Exception) {
            Log.e("signUp", "Error in signUpUseCase: ${e.message}", e)
            throw e
        }
    }


    sealed class SignUpException : Exception() {
        data object UsernameException : SignUpException()
        data object UserIdentityException : SignUpException()
        data object PasswordException : SignUpException()
        data object NoException : SignUpException()
    }
}

