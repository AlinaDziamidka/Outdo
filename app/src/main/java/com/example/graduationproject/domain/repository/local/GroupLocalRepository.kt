package com.example.graduationproject.domain.repository.local

import com.example.graduationproject.domain.entity.Group

interface GroupLocalRepository {

    suspend fun fetchAll(): List<Group>

    suspend fun fetchById(groupId: String): Group

    suspend fun insertOne(group: Group)

    suspend fun deleteById(groupId: String)

    suspend fun updateOne(group: Group)
}