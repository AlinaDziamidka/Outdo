package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.service.UsernameApiService
import com.example.graduationproject.domain.entity.Username
import com.example.graduationproject.domain.repository.UsernameRepository
import javax.inject.Inject

class UsernameRepositoryImpl @Inject constructor (private val usernameApiService: UsernameApiService) :
    UsernameRepository {
    override suspend fun fetchUsername(username: String): Username {
        val response = usernameApiService.fetchUsername(username)
        return Username(response.username)
    }
}