package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.request.ChallengeRequest
import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.transormer.ChallengeTransformer
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.repository.remote.ChallengeRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class ChallengeRemoteRepositoryImpl @Inject constructor(private val challengeApiService: ChallengeApiService) :
    ChallengeRemoteRepository {

    private val challengeTransformer = ChallengeTransformer()

    override suspend fun fetchChallengesById(challengeIdQuery: String): Event<Challenge> {
        val idQuery = "objectId=\'$challengeIdQuery\'"
        val event = doCall {
            return@doCall challengeApiService.fetchChallengesById(idQuery)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data.first()
                val challenge = challengeTransformer.fromResponse(response)
                Event.Success(challenge)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun fetchWeekChallenge(challengeTypeQuery: String): Event<Challenge> {
        val typeQuery = "challengeType = \'$challengeTypeQuery\'"
        val event = doCall {
            return@doCall challengeApiService.fetchWeekChallenge(typeQuery)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data.first()
                val challenge = challengeTransformer.fromResponse(response)
                Event.Success(challenge)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun fetchChallengesByStatusAndId(
        challengeIdQuery: String, challengeStatusQuery: String
    ): Event<Challenge> {
        val idQuery = "objectId=\'$challengeIdQuery\'"
        val statusQuery = "challengeStatus = \'$challengeStatusQuery\'"
        val event = doCall {
            return@doCall challengeApiService.fetchChallengesByStatusAndId(idQuery, statusQuery)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data.first()
                val challenge = challengeTransformer.fromResponse(response)
                Event.Success(challenge)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun insertChallenge(
        challengeName: String,
        creatorId: String,
        description: String,
        endTime: Long,
        startTime: Long
    ): Event<Challenge> {

        val event = doCall {
            val request = ChallengeRequest(
                challengeName = challengeName,
                creatorId = creatorId,
                challengeDescription = description,
                endTime = endTime,
                startTime = startTime,
                categoryId = null,
                challengeIcon = null,
                challengeType = ChallengeType.GROUP.stringValue,
                challengeStatus = ChallengeStatus.UNFINISHED.stringValue
            )
            return@doCall challengeApiService.insertChallenge(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val challenge = challengeTransformer.fromResponse(response)
                Event.Success(challenge)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}