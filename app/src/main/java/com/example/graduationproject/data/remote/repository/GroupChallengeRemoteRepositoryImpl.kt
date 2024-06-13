package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.response.ChallengeResponse
import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.api.service.GroupChallengeApiService
import com.example.graduationproject.data.remote.transormer.ChallengeTransformer
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.repository.remote.GroupChallengeRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class GroupChallengeRemoteRepositoryImpl @Inject constructor(
    private val groupChallengeApiService: GroupChallengeApiService,
    private val challengeApiService: ChallengeApiService
) :
    GroupChallengeRemoteRepository {

    override suspend fun fetchAllChallengesByGroupId(groupIdQuery: String): Event<List<Challenge>> {
        val query = "groupId=\'$groupIdQuery\'"
        Log.d("GroupChallengeRepositoryImpl", "Fetching all challenges by group ID: $groupIdQuery")
        val event = doCall {
            return@doCall groupChallengeApiService.fetchAllChallengesByGroupId(query)
        }

        return when (event) {
            is Event.Success -> {
                val challenges = mutableListOf<Challenge>()
                val response = event.data

                response.forEach { groupChallengeResponse ->
                    val challengeEvent = getChallengesById(groupChallengeResponse.challengeId)
                    if (challengeEvent is Event.Success) {
                        challenges.add(challengeEvent.data)
                    } else {
                        Log.e(
                            "GroupChallengeRepositoryImpl",
                            "Failed to fetch challenge with ID: ${groupChallengeResponse.challengeId}"
                        )
                        return Event.Failure("Not found challenges")
                    }
                }
                Log.d(
                    "GroupChallengeRepositoryImpl",
                    "Successfully fetched all challenges by group ID: $groupIdQuery"
                )
                Event.Success(challenges)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    private suspend fun getChallengesById(challengeId: String): Event<Challenge> {
        val idQuery = "objectId=\'$challengeId\'"
        Log.d("GroupChallengeRepositoryImpl", "Fetching challenge by ID: $challengeId")
        val event = doCall {
            return@doCall challengeApiService.fetchChallengesById(idQuery)
        }
        return getAllChallenges(event)
    }

    private fun getAllChallenges(
        event: Event<List<ChallengeResponse>>
    ): Event<Challenge> {
        return when (event) {
            is Event.Success -> {
                Log.d("GroupChallengeRepositoryImpl", "Received challenge: ${event.data}")
                val response = event.data.first()
                val challengeTransformer = ChallengeTransformer()
                val challenge = challengeTransformer.fromResponse(response)
                Log.d(
                    "GroupChallengeRepositoryImpl",
                    "Transformed response to challenge: $challenge"
                )
                Event.Success(challenge)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun fetchChallengesByGroupIdANDStatus(
        groupIdQuery: String,
        challengeStatusQuery: String
    ): Event<List<Challenge>> {
        val idQuery = "groupId=\'$groupIdQuery\'"
        val statusQuery = "challengeStatus = \'$challengeStatusQuery\'"
        val event = doCall {
            return@doCall groupChallengeApiService.fetchAllChallengesByGroupId(idQuery)
        }

        return when (event) {
            is Event.Success -> {
                Log.d("ChallengeRepositoryImpl", "Received response with STATUS: ${event.data}")
                val challenges = mutableListOf<Challenge>()

                val response = event.data
                response.forEach { groupChallengeResponse ->
                    val challengeEvent =
                        getChallengesByIdAndStatus(groupChallengeResponse.challengeId, statusQuery)
                    if (challengeEvent is Event.Success) {
                        challenges.add(challengeEvent.data)
                    } else {
                        Log.e(
                            "GroupChallengeRepositoryImpl",
                            "Failed to fetch challenge with ID and STATUS: ${groupChallengeResponse.challengeId}"
                        )
                        return Event.Failure("Not found challenges by STATUS")
                    }
                }
                Log.d(
                    "GroupChallengeRepositoryImpl",
                    "Successfully fetched all challenges by group ID and STATUS: $groupIdQuery"
                )
                Event.Success(challenges)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    private suspend fun getChallengesByIdAndStatus(
        challengeId: String,
        challengeStatus: String
    ): Event<Challenge> {

        val event = doCall {
            return@doCall challengeApiService.fetchChallengesByStatusAndId(
                challengeId,
                challengeStatus
            )
        }

        return when (event) {
            is Event.Success -> {
                Log.d(
                    "GroupChallengeRepositoryImpl",
                    "Received response with STATUS: ${event.data}"
                )
                val response = event.data.first()
                val challengeTransformer = ChallengeTransformer()
                val challenge = challengeTransformer.fromResponse(response)
                Log.d(
                    "GroupChallengeRepositoryImpl",
                    "Transformed response to challenge with STATUS: $challenge"
                )
                Event.Success(challenge)
            }

            is Event.Failure -> {
                Log.e(
                    "GroupChallengeRepositoryImpl",
                    "Failed to fetch challenges with STATUS: ${event.exception}"
                )
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}
