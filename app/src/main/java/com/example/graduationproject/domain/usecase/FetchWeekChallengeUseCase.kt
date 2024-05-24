package com.example.graduationproject.domain.usecase

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.LoadManager
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchWeekChallengeUseCase @Inject constructor(
    @Remote private val remoteLoadManager: LoadManager,
    @Local private val localLoadManager: LoadManager
) : UseCase<FetchWeekChallengeUseCase.Params, Challenge> {

    data class Params(
        val challengeType: ChallengeType,
    )

    override suspend fun invoke(params: Params): Flow<Challenge> =
        flow {
            val challengeTypeQuery = params.challengeType
            var event = localLoadManager.fetchWeekChallenge(challengeTypeQuery)

            if (event is Event.Failure) {
                event = remoteLoadManager.fetchWeekChallenge(challengeTypeQuery)
            }

            when (event) {
                is Event.Success -> emit(event.data)
                is Event.Failure -> throw Exception(event.exception)
            }
        }
}
