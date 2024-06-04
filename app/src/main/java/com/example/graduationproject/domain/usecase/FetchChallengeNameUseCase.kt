package com.example.graduationproject.domain.usecase

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchChallengeNameUseCase @Inject constructor(
    private val challengeLocalRepository: ChallengeLocalRepository
) : UseCase<FetchChallengeNameUseCase.Params, Challenge> {

    data class Params(
        val challengeId: String
    )

    override suspend fun invoke(params: Params): Flow<Challenge> =
        flow {
            emit(withContext(Dispatchers.IO) {
                challengeLocalRepository.fetchById(params.challengeId)
            })
        }
}