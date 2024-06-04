package com.example.graduationproject.data.local.repository

import com.example.graduationproject.data.local.database.dao.ChallengeAchievementDao
import com.example.graduationproject.data.local.transformer.ChallengeAchievementTransformer
import com.example.graduationproject.domain.entity.ChallengeAchievement
import com.example.graduationproject.domain.repository.local.ChallengeAchievementsLocalRepository
import javax.inject.Inject

class ChallengeAchievementsLocalRepositoryImpl @Inject constructor(private val challengeAchievementDao: ChallengeAchievementDao) :
    ChallengeAchievementsLocalRepository {

    private val challengeAchievementTransformer = ChallengeAchievementTransformer()

    override suspend fun fetchAll(): List<ChallengeAchievement> {
        val models = challengeAchievementDao.fetchAll()
        return models.map {
            challengeAchievementTransformer.fromModel(it)
        }
    }

    override suspend fun fetchAchievementsByChallengeId(challengeId: String): List<ChallengeAchievement> {
        val model = challengeAchievementDao.fetchAchievementsByChallengeId(challengeId)
        return model.map {
            challengeAchievementTransformer.fromModel(it)
        }
    }

    override suspend fun fetchChallengesByChallengeId(challengeId: String): List<ChallengeAchievement> {
        val model = challengeAchievementDao.fetchChallengesByChallengeId(challengeId)
        return model.map {
            challengeAchievementTransformer.fromModel(it)
        }
    }

    override suspend fun insertOne(challengeAchievement: ChallengeAchievement) {
        val model = challengeAchievementTransformer.toModel(challengeAchievement)
        return challengeAchievementDao.insertOne(model)
    }

    override suspend fun deleteOne(challengeAchievement: ChallengeAchievement) {
        val model = challengeAchievementTransformer.toModel(challengeAchievement)
        return challengeAchievementDao.deleteOne(model)
    }

    override suspend fun updateOne(challengeAchievement: ChallengeAchievement) {
        val model = challengeAchievementTransformer.toModel(challengeAchievement)
        return challengeAchievementDao.updateOne(model)
    }
}