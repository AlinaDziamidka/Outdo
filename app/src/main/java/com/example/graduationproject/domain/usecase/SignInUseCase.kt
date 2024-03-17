package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.entity.Session
import com.example.graduationproject.domain.repository.SessionRepository
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
        Log.d("SignInUseCase", "User identity: $userIdentity, Password: $password")
        if (userIdentity.isNotEmpty() && password.isNotEmpty()) {
            Log.d("SignInUseCase", "Starting sign in process with user identity: $userIdentity")
            val event =
                sessionRepository.signIn(userIdentity = userIdentity, password = password)
            Log.d("SignInUseCase", "Sign in event: $event")
            when (event) {
                is Event.Success -> {
                    val session = event.data
                    sessionRepository.saveToken(session.token)
                    sessionRepository.saveUserProfile(session.userProfile)
                    Log.d("SignInUseCase", "SignIn success: $session")
                    emit(session)
                }

                is Event.Failure -> {
                    val error = event.exception
                    Log.e("SignInUseCase", "SignIn failure: ${error.toString()}")
                    throw Exception(event.exception)
                }
            }
        } else {
            val errorMessage = "Email or password is wrong."
            Log.e("SignInUseCase", errorMessage)
            throw Exception("Email or password is wrong.")
        }
    }
}


//object UserManager {
//    private var userId: Long? = null
//
//    fun getUserId(): Long? {
//        return userId
//    }
//
//    fun setUserId(id: Long) {
//        userId = id
//    }
//}