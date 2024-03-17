package com.example.graduationproject.domain.repository
import com.example.graduationproject.domain.entity.Username

interface UsernameRepository {
    suspend fun fetchUsername(username: String): Username
}