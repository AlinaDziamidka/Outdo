package com.example.graduationproject.domain.repository.remote
import com.example.graduationproject.domain.entity.UserProfile


interface UserRepository {
    suspend fun fetchUsersByUsername(usernameQuery: String): List<UserProfile>
    suspend fun fetchUsersByIdentity(userIdentityQuery: String): List<UserProfile>
}