package com.example.graduationproject.domain.usecase

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchChallengeDescriptionUseCase @Inject constructor(
    private val challengeLocalRepository: ChallengeLocalRepository,
    private val userLocalRepository: UserLocalRepository
) : UseCase<FetchChallengeDescriptionUseCase.Params, Pair<Challenge, UserProfile?>> {

    data class Params(
        val challengeId: String
    )

    override suspend fun invoke(params: Params): Flow<Pair<Challenge, UserProfile?>> =
        flow {
            val challenge = challengeLocalRepository.fetchById(params.challengeId)
            val creator = challenge.creatorId?.let { userLocalRepository.fetchById(it) }
            emit(Pair(challenge, creator))
        }.flowOn(Dispatchers.IO)
}