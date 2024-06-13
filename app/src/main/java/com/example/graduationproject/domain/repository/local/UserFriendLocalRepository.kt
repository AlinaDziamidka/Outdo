package com.example.graduationproject.domain.repository.local

import com.example.graduationproject.domain.entity.UserFriend

interface UserFriendLocalRepository {

    suspend fun fetchAll(): List<UserFriend>

    suspend fun fetchFriendsByUserId(userId: String): List<UserFriend>

    suspend fun insertOne(userFriend: UserFriend)

    suspend fun deleteOne(userFriend: UserFriend)

    suspend fun updateOne(userFriend: UserFriend)
}