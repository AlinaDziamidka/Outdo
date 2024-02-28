package com.example.graduationproject.domain.usecase

import com.example.graduationproject.domain.repository.SessionRepository

class SignInUseCase (private val sessionRepository: SessionRepository) {
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