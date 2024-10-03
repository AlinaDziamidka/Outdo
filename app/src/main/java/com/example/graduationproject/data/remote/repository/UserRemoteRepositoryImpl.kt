package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.UserApiService
import com.example.graduationproject.data.remote.transormer.UserTransformer
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class UserRemoteRepositoryImpl @Inject constructor(private val userApiService: UserApiService) :
    UserRemoteRepository {
    override suspend fun fetchUsersByUsername(usernameQuery: String): List<UserProfile> {
        val query = "username=\'$usernameQuery\'"
        val response = userApiService.fetchUsersByUsername(query)
        return response.map { userResponse ->
            val userTransformer = UserTransformer()
            userTransformer.fromResponse(userResponse)
        }
    }

    override suspend fun fetchUsersByEmail(userEmailQuery: String): List<UserProfile> {
        val query = "email=\'$userEmailQuery\'"
        val response = userApiService.fetchUsersByEmail(query)
        return response.map { userResponse ->
            val userTransformer = UserTransformer()
            userTransformer.fromResponse(userResponse)
        }
    }

    override suspend fun fetchUserById(userIdQuery: String): Event<UserProfile> {
        val query = "objectId=\'$userIdQuery\'"
        val event = doCall {
            return@doCall userApiService.fetchUsersById(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data.first()
                val userTransformer = UserTransformer()
                val user = userTransformer.fromResponse(response)
                Event.Success(user)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}