package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.request.GroupChallengeRequest
import com.example.graduationproject.data.remote.api.response.ChallengeResponse
import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.api.service.GroupChallengeApiService
import com.example.graduationproject.data.remote.transormer.ChallengeTransformer
import com.example.graduationproject.data.remote.transormer.GroupChallengeTransformer
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.repository.remote.GroupChallengeRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class GroupChallengeRemoteRepositoryImpl @Inject constructor(
    private val groupChallengeApiService: GroupChallengeApiService,
    private val challengeApiService: ChallengeApiService
) : GroupChallengeRemoteRepository {

    override suspend fun fetchAllChallengesByGroupId(groupIdQuery: String): Event<List<Challenge>> {
        val query = "groupId=\'$groupIdQuery\'"
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
                        return Event.Failure("Not found challenges")
                    }
                }
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

    override suspend fun fetchChallengesByGroupIdANDStatus(
        groupIdQuery: String, challengeStatusQuery: String
    ): Event<List<Challenge>> {
        val idQuery = "groupId=\'$groupIdQuery\'"
        val statusQuery = "challengeStatus = \'$challengeStatusQuery\'"
        val event = doCall {
            return@doCall groupChallengeApiService.fetchAllChallengesByGroupId(idQuery)
        }

        return when (event) {
            is Event.Success -> {
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
                    }
                }
                Event.Success(challenges)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    private suspend fun getChallengesByIdAndStatus(
        challengeId: String, challengeStatus: String
    ): Event<Challenge> {

        val event = doCall {
            return@doCall challengeApiService.fetchChallengesByStatusAndId(
                challengeId, challengeStatus
            )
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

    override suspend fun insertGroupChallenge(
        groupId: String, challengeId: String
    ): Event<GroupChallenge> {
        val event = doCall {
            val request = GroupChallengeRequest(groupId, challengeId)
            return@doCall groupChallengeApiService.insertGroupChallenge(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val groupChallengeTransformer = GroupChallengeTransformer()
                val groupChallenge = groupChallengeTransformer.fromResponse(response)
                Event.Success(groupChallenge)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}
