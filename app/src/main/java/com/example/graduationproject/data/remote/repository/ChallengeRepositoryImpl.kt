package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.transormer.ChallengeTransformer
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.repository.remote.ChallengeRepository
import com.example.graduationproject.domain.util.Event
import doCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ChallengeRepositoryImpl(private val challengeApiService: ChallengeApiService) :
    ChallengeRepository {

    override suspend fun fetchChallengesById(challengeIdQuery: String): Event<Challenge> {
        val idQuery = "objectId=\'$challengeIdQuery\'"
        val event = doCall {
            return@doCall challengeApiService.fetchChallengesById(idQuery)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data.first()
                val challengeTransformer = ChallengeTransformer()
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
                val challengeTransformer = ChallengeTransformer()
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