package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.service.UserApiService
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userApiService: UserApiService) :
    UserRepository {
    override suspend fun fetchUsersByUsername(usernameQuery: String): List<UserProfile> {
        val query = "username=\'$usernameQuery\'"
        val response = userApiService.fetchUsersByUsername(query)
        return response.map { userResponse ->
            UserProfile(
                userId = userResponse.userId,
                username = userResponse.username,
                userIdentity = userResponse.userIdentity,
                userAvatarPath = userResponse.userAvatarPath
            )
        }
    }
}