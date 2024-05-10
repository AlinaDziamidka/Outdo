package com.example.graduationproject.data.local.repository

import com.example.graduationproject.data.local.database.dao.AchievementDao
import com.example.graduationproject.data.local.transformer.AchievementTransformer
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.repository.local.AchievementLocalRepository
import javax.inject.Inject

class AchievementLocalRepositoryImpl @Inject constructor(private val achievementDao: AchievementDao) :
    AchievementLocalRepository {

    private val achievementTransformer = AchievementTransformer()

    override suspend fun fetchAll(): List<Achievement> {
        val models = achievementDao.fetchAll()
        return models.map {
            achievementTransformer.fromModel(it)
        }
    }

    override suspend fun fetchById(achievementId: String): Achievement {
        val model = achievementDao.fetchById(achievementId)
        return achievementTransformer.fromModel(model)
    }

    override suspend fun fetchDailyAchievement(achievementType: String): Achievement {
        val model = achievementDao.fetchDailyAchievement(achievementType)
        return achievementTransformer.fromModel(model)
    }

    override suspend fun insertOne(achievement: Achievement) {
        val model = achievementTransformer.toModel(achievement)
        achievementDao.insertOne(model)
    }

    override fun deleteById(achievementId: String) = achievementDao.deleteById(achievementId)

    override fun updateOne(achievement: Achievement) {
        val model = achievementTransformer.toModel(achievement)
        achievementDao.updateOne(model)
    }


}