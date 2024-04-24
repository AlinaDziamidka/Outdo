package com.example.graduationproject.data.local.repository

import com.example.graduationproject.data.local.database.dao.GroupChallengeDao
import com.example.graduationproject.data.local.database.dao.GroupDao
import com.example.graduationproject.data.local.database.model.GroupChallengeModel
import com.example.graduationproject.data.local.transformer.GroupChallengeTransformer
import com.example.graduationproject.data.local.transformer.UserGroupTransformer
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.repository.local.GroupChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import javax.inject.Inject

class GroupChallengeLocalRepositoryImpl @Inject constructor(private val groupChallengeDao: GroupChallengeDao) :
    GroupChallengeLocalRepository {

    private val groupChallengeTransformer = GroupChallengeTransformer()

    override suspend fun fetchAll(): List<GroupChallenge> {
        val models = groupChallengeDao.fetchAll()
        return models.map {
            groupChallengeTransformer.fromModel(it)
        }
    }

    override suspend fun fetchGroupsByChallengeId(challengeId: String): List<GroupChallenge> {
        val model = groupChallengeDao.fetchGroupsByChallengeId(challengeId)
        return model.map {
            groupChallengeTransformer.fromModel(it)
        }
    }

    override suspend fun fetchChallengesByGroupId(groupId: String): List<GroupChallenge> {
        val model = groupChallengeDao.fetchChallengesByGroupId(groupId)
        return model.map {
            groupChallengeTransformer.fromModel(it)
        }
    }

    override suspend fun insertOne(groupChallenge: GroupChallenge) {
        val model = groupChallengeTransformer.toModel(groupChallenge)
        return groupChallengeDao.insertOne(model)
    }

    override suspend fun deleteOne(groupChallenge: GroupChallenge) {
        val model = groupChallengeTransformer.toModel(groupChallenge)
        return groupChallengeDao.deleteOne(model)
    }

    override suspend fun updateOne(groupChallenge: GroupChallenge) {
        val model = groupChallengeTransformer.toModel(groupChallenge)
        return groupChallengeDao.updateOne(model)
    }
}