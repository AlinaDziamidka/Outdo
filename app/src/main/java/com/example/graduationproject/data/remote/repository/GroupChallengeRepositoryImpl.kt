package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.api.service.GroupChallengeApiService
import com.example.graduationproject.data.remote.transormer.ChallengeTransformer
import com.example.graduationproject.data.remote.transormer.GroupChallengeTransformer
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.repository.remote.GroupChallengeRepository
import com.example.graduationproject.domain.util.Event
import doCall
import kotlinx.coroutines.flow.map

class GroupChallengeRepositoryImpl(
    private val groupChallengeApiService: GroupChallengeApiService,
    private val challengeApiService: ChallengeApiService
) :
    GroupChallengeRepository {

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
                    val challengeEvent = getChallenges(groupChallengeResponse.challengeId)
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

    private suspend fun getChallenges(challengeId: String): Event<Challenge> {
        val query = "objectId=\'$challengeId\'"
        val event = doCall {
            return@doCall challengeApiService.fetchChallengesById(query)
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
