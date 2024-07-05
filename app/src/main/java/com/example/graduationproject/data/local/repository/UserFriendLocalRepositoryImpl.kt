package com.example.graduationproject.data.local.repository

import android.util.Log
import com.example.graduationproject.data.local.database.dao.UserFriendDao
import com.example.graduationproject.data.local.transformer.UserFriendTransformer
import com.example.graduationproject.domain.entity.UserFriend
import com.example.graduationproject.domain.repository.local.UserFriendLocalRepository
import javax.inject.Inject

class UserFriendLocalRepositoryImpl @Inject constructor(private val userFriendDao: UserFriendDao) :
    UserFriendLocalRepository {

    private val userFriendTransformer = UserFriendTransformer()

    override suspend fun fetchAll(): List<UserFriend> {
        val models = userFriendDao.fetchAll()
        return models.map {
            userFriendTransformer.fromModel(it)
        }
    }

    override suspend fun fetchFriendsByUserId(userId: String): List<UserFriend> {
        Log.d("UserFriendLocalRepositoryImpl", userId)
        val models = userFriendDao.fetchFriendsByUserId(userId)
        models.forEach{
        Log.d("UserFriendLocalRepositoryImpl", "${it.friendId}, ${it.userId} ")
    }
        return models.map {
            userFriendTransformer.fromModel(it)
        }
    }

    override suspend fun insertOne(userFriend: UserFriend) {
        val model = userFriendTransformer.toModel(userFriend)
        return userFriendDao.insertOne(model)
    }

    override suspend fun deleteOne(userFriend: UserFriend) {
        val model = userFriendTransformer.toModel(userFriend)
        return userFriendDao.deleteOne(model)
    }

    override suspend fun updateOne(userFriend: UserFriend) {
        val model = userFriendTransformer.toModel(userFriend)
        return userFriendDao.updateOne(model)
    }
}