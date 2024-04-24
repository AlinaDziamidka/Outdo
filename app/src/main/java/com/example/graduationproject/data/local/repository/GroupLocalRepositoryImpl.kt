package com.example.graduationproject.data.local.repository

import com.example.graduationproject.data.local.database.dao.GroupDao
import com.example.graduationproject.data.local.transformer.GroupTransformer
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import javax.inject.Inject

class GroupLocalRepositoryImpl @Inject constructor(private val groupDao: GroupDao) :
    GroupLocalRepository {


    private val groupTransformer = GroupTransformer()
    override suspend fun fetchAll(): List<Group> {
        val model = groupDao.fetchAll()
        return model.map {
            groupTransformer.fromModel(it)
        }
    }

    override suspend fun fetchById(groupId: String): Group {
        val model = groupDao.fetchById(groupId)
        return groupTransformer.fromModel(model)
    }

    override suspend fun insertOne(group: Group) {
        val model = groupTransformer.toModel(group)
        return groupDao.insertOne(model)
    }

    override suspend fun deleteById(groupId: String) = groupDao.deleteById(groupId)

    override suspend fun updateOne(group: Group) {
        val model = groupTransformer.toModel(group)
        return groupDao.updateOne(model)
    }
}