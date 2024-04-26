package com.example.graduationproject.domain.usecase.local

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchLocalWeekChallengeUseCase @Inject constructor(
    private val challengeLocalRepository: ChallengeLocalRepository
) : UseCase<FetchLocalWeekChallengeUseCase.Params, Challenge> {

    data class Params(
        val challengeType: ChallengeType,
    )

    override suspend fun invoke(params: FetchLocalWeekChallengeUseCase.Params): Flow<Challenge> =
        flow {
            val challengeTypeQuery = params.challengeType
            val challenge = challengeLocalRepository.fetchWeekChallenge(challengeTypeQuery.stringValue)
            emit(challenge)
    }
}