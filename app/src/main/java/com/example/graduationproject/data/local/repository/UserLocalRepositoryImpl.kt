package com.example.graduationproject.data.local.repository

import android.util.Log
import com.example.graduationproject.data.local.database.dao.UserDao
import com.example.graduationproject.data.local.transformer.UserTransformer

import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import javax.inject.Inject

class UserLocalRepositoryImpl @Inject constructor(private val userDao: UserDao) :
    UserLocalRepository {

    private val userTransformer = UserTransformer()

    override suspend fun fetchAll(): List<UserProfile> {
        val model = userDao.fetchAll()
        return model.map {
            userTransformer.fromModel(it)
        }
    }

    override suspend fun fetchById(userId: String): UserProfile {
        val model = userDao.fetchById(userId)
            Log.d("UserLocalRepositoryImpl", "${model.userId}, ${model.userEmail}, ${model.userAvatarPath}, ${model.username} ")
        return userTransformer.fromModel(model)
    }

    override suspend fun insertOne(user: UserProfile) {
        val model = userTransformer.toModel(user)
        return userDao.insertOne(model)
    }

    override suspend fun deleteById(userId: String) = userDao.deleteById(userId)

    override suspend fun updateOne(user: UserProfile) {
        val model = userTransformer.toModel(user)
        return userDao.updateOne(model)
    }
}