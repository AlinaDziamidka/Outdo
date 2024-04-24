package com.example.graduationproject.domain.repository.local

import com.example.graduationproject.data.local.database.model.UserGroupModel
import com.example.graduationproject.domain.entity.UserGroup

interface UserGroupLocalRepository {

    suspend fun fetchAll(): List<UserGroup>

    suspend fun fetchGroupsByUserId(userId: String): List<UserGroup>

    suspend fun fetchUsersByGroupId(groupId: String): List<UserGroup>

    suspend fun insertOne(userGroup: UserGroup)

    suspend fun deleteById(userGroup: UserGroup)

    suspend fun updateOne(userGroup: UserGroup)

}