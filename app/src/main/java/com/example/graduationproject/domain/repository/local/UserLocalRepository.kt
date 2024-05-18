package com.example.graduationproject.domain.repository.local

import com.example.graduationproject.domain.entity.UserProfile

interface UserLocalRepository {

    suspend fun fetchAll(): List<UserProfile>

    suspend fun fetchById(userId: String): UserProfile

    suspend fun insertOne(user: UserProfile)

    suspend fun deleteById(userId: String)

   suspend fun updateOne(user: UserProfile)
}