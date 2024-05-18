package com.example.graduationproject.domain.repository.remote
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.util.Event
import retrofit2.Response


interface UserRemoteRepository {
    suspend fun fetchUsersByUsername(usernameQuery: String): List<UserProfile>

    suspend fun fetchUsersByEmail(userEmailQuery: String): List<UserProfile>

    suspend fun fetchUserById(userIdQuery: String): Event<UserProfile>
}