package com.example.graduationproject.data.local.repository


import com.example.graduationproject.data.local.database.dao.UserGroupDao
import com.example.graduationproject.data.local.database.model.UserGroupModel
import com.example.graduationproject.data.local.transformer.UserGroupTransformer
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository

import javax.inject.Inject

class UserGroupLocalRepositoryImpl @Inject constructor(private val userGroupDao: UserGroupDao) :
    UserGroupLocalRepository {

    private val userGroupTransformer = UserGroupTransformer()

    override suspend fun fetchAll(): List<UserGroup> {
        val models = userGroupDao.fetchAll()
        return models.map {
            userGroupTransformer.fromModel(it)
        }
    }

    override suspend fun fetchGroupsByUserId(userId: String): List<UserGroup> {
        val models = userGroupDao.fetchGroupsByUserId(userId)
        return models.map {
            userGroupTransformer.fromModel(it)
        }
    }

    override suspend fun fetchUsersByGroupId(groupId: String): List<UserGroup> {
        val models = userGroupDao.fetchUsersByGroupId(groupId)
        return models.map {
            userGroupTransformer.fromModel(it)
        }
    }

    override suspend fun insertOne(userGroup: UserGroup) {
        val model = userGroupTransformer.toModel(userGroup)
        return userGroupDao.insertOne(model)
    }

    override suspend fun deleteById(userGroup: UserGroup) {
        val model = userGroupTransformer.toModel(userGroup)
        return userGroupDao.deleteOne(model)
    }


    override suspend fun updateOne(userGroup: UserGroup) {
        val model = userGroupTransformer.toModel(userGroup)
        return userGroupDao.updateOne(model)
    }
}
