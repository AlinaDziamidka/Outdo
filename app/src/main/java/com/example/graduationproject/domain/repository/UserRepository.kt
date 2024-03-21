package com.example.graduationproject.domain.repository
import com.example.graduationproject.domain.entity.UserProfile


interface UserRepository {
    suspend fun fetchUsersByUsername(usernameQuery: String): List<UserProfile>
    suspend fun fetchUsersByIdentity(userIdentityQuery: String): List<UserProfile>
}