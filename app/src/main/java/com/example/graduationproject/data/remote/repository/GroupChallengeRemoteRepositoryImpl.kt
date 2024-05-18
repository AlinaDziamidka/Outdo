package com.example.graduationproject.data.remote.repository

import android.util.Log
import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.api.service.GroupChallengeApiService
import com.example.graduationproject.data.remote.transormer.ChallengeTransformer
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.repository.remote.GroupChallengeRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall

class GroupChallengeRemoteRepositoryImpl(
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
                    val challengeEvent = getChallenges(groupChallengeResponse.challengeId)
                    if (challengeEvent is Event.Success) {
                        challenges.add(challengeEvent.data)
                    } else {
                        Log.e("GroupChallengeRepositoryImpl", "Failed to fetch challenge with ID: ${groupChallengeResponse.challengeId}")
                        return Event.Failure("Not found challenges")
                    }
                }
                Log.d("GroupChallengeRepositoryImpl", "Successfully fetched all challenges by group ID: $groupIdQuery")
                Event.Success(challenges)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    private suspend fun getChallenges(challengeId: String): Event<Challenge> {
        val idQuery = "objectId=\'$challengeId\'"
        Log.d("GroupChallengeRepositoryImpl", "Fetching challenge by ID: $challengeId")
        val event = doCall {
            return@doCall challengeApiService.fetchChallengesById(idQuery)
        }

        return when (event) {
            is Event.Success -> {
                Log.d("GroupChallengeRepositoryImpl", "Received challenge: ${event.data}")
                val response = event.data.first()
                val challengeTransformer = ChallengeTransformer()
                val challenge = challengeTransformer.fromResponse(response)
                Log.d("GroupChallengeRepositoryImpl", "Transformed response to challenge: $challenge")
                Event.Success(challenge)
            }

            is Event.Failure -> {
                val error = event.exception
                Log.e("GroupChallengeRepositoryImpl", "Failed to fetch challenge with ID: $challengeId, Error: $error")
                Event.Failure(error)
            }
        }
    }
}
