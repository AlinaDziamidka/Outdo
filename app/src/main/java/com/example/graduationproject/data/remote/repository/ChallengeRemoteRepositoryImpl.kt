package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.transormer.ChallengeTransformer
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.repository.remote.ChallengeRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class ChallengeRemoteRepositoryImpl @Inject constructor(private val challengeApiService: ChallengeApiService) :
    ChallengeRemoteRepository {

    override suspend fun fetchChallengesById(challengeIdQuery: String): Event<Challenge> {
        val idQuery = "objectId=\'$challengeIdQuery\'"
        Log.d("ChallengeRepositoryImpl", "Fetching challenge with query: $idQuery")
        val event = doCall {
            return@doCall challengeApiService.fetchChallengesById(idQuery)
        }

        return when (event) {
            is Event.Success -> {
                Log.d("ChallengeRepositoryImpl", "Received response: ${event.data}")
                val response = event.data.first()
                val challengeTransformer = ChallengeTransformer()
                val challenge = challengeTransformer.fromResponse(response)
                Log.d("ChallengeRepositoryImpl", "Transformed response to challenge: $challenge")
                Event.Success(challenge)
            }

            is Event.Failure -> {
                Log.e("ChallengeRepositoryImpl", "Failed to fetch challenges: ${event.exception}")
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

    override suspend fun fetchChallengesByStatusAndId(
        challengeIdQuery: String,
        challengeStatusQuery: String
    ): Event<Challenge> {
        val idQuery = "objectId=\'$challengeIdQuery\'"
        val statusQuery = "challengeStatus = \'$challengeStatusQuery\'"
        val event = doCall {
            return@doCall challengeApiService.fetchChallengesByStatusAndId(idQuery, statusQuery)
        }

        return when (event) {
            is Event.Success -> {
                Log.d("ChallengeRepositoryImpl", "Received response with STATUS: ${event.data}")
                val response = event.data.first()
                val challengeTransformer = ChallengeTransformer()
                val challenge = challengeTransformer.fromResponse(response)
                Log.d("ChallengeRepositoryImpl", "Transformed response to challenge with STATUS: $challenge")
                Event.Success(challenge)
            }

            is Event.Failure -> {
                Log.e("ChallengeRepositoryImpl", "Failed to fetch challenges with STATUS: ${event.exception}")
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}