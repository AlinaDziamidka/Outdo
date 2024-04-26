package com.example.graduationproject.domain.usecase.remote

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.remote.ChallengeRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import com.example.graduationproject.domain.util.writeToLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchRemoteWeekChallengeUseCase @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val challengeLocalRepository: ChallengeLocalRepository
) : UseCase<FetchRemoteWeekChallengeUseCase.Params, Challenge> {

    data class Params(
        val challengeType: ChallengeType,
    )

    override suspend fun invoke(params: Params): Flow<Challenge> =
        flow {
            val challengeTypeQuery = params.challengeType
            val challenge = getWeekChallenge(challengeTypeQuery)
            emit(challenge)
        }

    private suspend fun getWeekChallenge(challengeType: ChallengeType): Challenge =
        withContext(Dispatchers.IO) {
            val event = challengeRepository.fetchWeekChallenge(challengeType.stringValue)
            when (event) {
                is Event.Success -> {
                    writeToLocalDatabase(challengeLocalRepository::insertOne, event.data)
                    event.data
                }

                is Event.Failure -> throw Exception(event.exception)
            }
        }
}
