package com.example.graduationproject.data.local.repository

import com.example.graduationproject.data.local.database.dao.UserDao
import com.example.graduationproject.data.local.database.model.UserModel
import com.example.graduationproject.domain.repository.local.UserLocalRepository

import javax.inject.Inject

class UserLocalRepositoryImpl @Inject constructor(private val userDao: UserDao) :
    UserLocalRepository {

    override suspend fun fetchAll(): List<UserModel> {
        return userDao.fetchAll()
    }

    override suspend fun fetchById(userId: String): UserModel {
        return userDao.fetchById(userId)
    }

    override suspend fun insertOne(userModel: UserModel) {
        userDao.insertOne(userModel)
    }

    override suspend fun deleteById(userId: String) {
        userDao.deleteById(userId)
    }

    override suspend fun updateOne(userModel: UserModel) {
        userDao.updateOne(userModel)
    }
}
