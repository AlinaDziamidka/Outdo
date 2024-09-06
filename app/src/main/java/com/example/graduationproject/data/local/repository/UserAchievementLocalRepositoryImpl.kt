package com.example.graduationproject.data.local.repository

import com.example.graduationproject.data.local.database.dao.UserAchievementDao
import com.example.graduationproject.data.local.transformer.UserAchievementTransformer
import com.example.graduationproject.domain.entity.AchievementStatus
import com.example.graduationproject.domain.entity.UserAchievement
import com.example.graduationproject.domain.repository.local.UserAchievementLocalRepository
import javax.inject.Inject

class UserAchievementLocalRepositoryImpl @Inject constructor(private val userAchievementDao: UserAchievementDao) :
    UserAchievementLocalRepository {

    private val userAchievementTransformer = UserAchievementTransformer()

    override suspend fun fetchAll(): List<UserAchievement> {
        val models = userAchievementDao.fetchAll()
        return models.map {
            userAchievementTransformer.fromModel(it)
        }
    }

    override suspend fun fetchAchievementsByUserId(userId: String): List<UserAchievement> {
        val models = userAchievementDao.fetchAchievementsByUserId(userId)
        return models.map {
            userAchievementTransformer.fromModel(it)
        }
    }

    override suspend fun fetchUsersByAchievementId(achievementId: String): List<UserAchievement> {
        val models = userAchievementDao.fetchAchievementsByUserId(achievementId)
        return models.map {
            userAchievementTransformer.fromModel(it)
        }
    }

    override suspend fun insertOne(userAchievement: UserAchievement) {
        val model = userAchievementTransformer.toModel(userAchievement)
        return userAchievementDao.insertOne(model)
    }

    override suspend fun deleteOne(userAchievement: UserAchievement) {
        val model = userAchievementTransformer.toModel(userAchievement)
        return userAchievementDao.deleteOne(model)
    }

    override suspend fun updateOne(userAchievement: UserAchievement) {
        val model = userAchievementTransformer.toModel(userAchievement)
        return userAchievementDao.updateOne(model)
    }

    override suspend fun updatePhoto(photoUrl: String?, achievementId: String, userId: String) =
        userAchievementDao.updatePhoto(
            AchievementStatus.COMPLETED.stringValue,
            photoUrl,
            achievementId,
            userId
        )
}