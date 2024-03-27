package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.UserApiService
import com.example.graduationproject.data.remote.transormer.UserGroupTransformer
import com.example.graduationproject.data.remote.transormer.UserTransformer
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.remote.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userApiService: UserApiService) :
    UserRepository {
    override suspend fun fetchUsersByUsername(usernameQuery: String): List<UserProfile> {
        val query = "username=\'$usernameQuery\'"
        val response = userApiService.fetchUsersByUsername(query)
        return response.map { userResponse ->
            val userTransformer = UserTransformer()
            userTransformer.fromResponse(userResponse)
        }
    }

    override suspend fun fetchUsersByIdentity(userIdentityQuery: String): List<UserProfile> {
        val query = "email=\'$userIdentityQuery\'"
        val response = userApiService.fetchUsersByIdentity(query)
        return response.map { userResponse ->
            val userTransformer = UserTransformer()
            userTransformer.fromResponse(userResponse)
        }
    }
}