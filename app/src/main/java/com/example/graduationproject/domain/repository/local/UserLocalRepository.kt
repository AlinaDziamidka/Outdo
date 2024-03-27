package com.example.graduationproject.domain.repository.local
import com.example.graduationproject.data.local.database.model.UserModel

interface UserLocalRepository {

   suspend fun fetchAll(): List<UserModel>

   suspend fun fetchById(userId: String): UserModel

    suspend fun insertOne(userModel: UserModel)

    suspend fun deleteById(userId: String)

    suspend fun updateOne(userModel: UserModel)

}