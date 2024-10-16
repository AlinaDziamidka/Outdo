package com.example.graduationproject.data.local.repository

import com.example.graduationproject.data.local.database.dao.ChallengeDao
import com.example.graduationproject.data.local.transformer.ChallengeTransformer
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.util.Event
import javax.inject.Inject

class ChallengeLocalRepositoryImpl @Inject constructor(private val challengeDao: ChallengeDao) :
    ChallengeLocalRepository {

    private val challengeTransformer = ChallengeTransformer()
    override suspend fun fetchAll(): List<Challenge> {
        val models = challengeDao.fetchAll()
        return models.map {
            challengeTransformer.fromModel(it)
        }
    }

    override suspend fun fetchById(challengeId: String): Challenge {
        val model = challengeDao.fetchById(challengeId)
        return challengeTransformer.fromModel(model)
    }

    override suspend fun fetchWeekChallenge(challengeType: String): Event<Challenge> {
        val model = challengeDao.fetchWeekChallenge(challengeType)

        return if (model != null) {
            val challenge = challengeTransformer.fromModel(model)
            Event.Success(challenge)
        } else {
            Event.Failure("Week not found")
        }
    }

    override suspend fun fetchChallengeSByStatusAndId(
        challengeId: String,
        challengeStatus: String
    ): Event<Challenge> {
        val model = challengeDao.fetchChallengesByStatusAndID(challengeId, challengeStatus)
        return if (model != null) {
            val challenge = challengeTransformer.fromModel(model)
            Event.Success(challenge)
        } else {
            Event.Failure("Challenge not found")
        }
    }

    override suspend fun insertOne(challenge: Challenge) {
        val model = challengeTransformer.toModel(challenge)
        challengeDao.insertOne(model)
    }

    override suspend fun deleteById(challengeId: String) = challengeDao.deleteById(challengeId)

    override suspend fun updateOne(challenge: Challenge) {
        val model = challengeTransformer.toModel(challenge)
        challengeDao.updateOne(model)
    }
}