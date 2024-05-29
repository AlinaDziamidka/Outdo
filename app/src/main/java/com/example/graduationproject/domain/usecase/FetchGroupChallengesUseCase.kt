package com.example.graduationproject.domain.usecase

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.util.LoadManager
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchGroupChallengesUseCase @Inject constructor(
    @Remote private val remoteLoadManager: LoadManager,
    @Local private val localLoadManager: LoadManager
) : UseCase<FetchGroupChallengesUseCase.Params, MutableList<Challenge>> {

    data class Params(
        val groupId: String,
        val challengeStatus: ChallengeStatus
    )

    override suspend fun invoke(params: Params): Flow<MutableList<Challenge>> =
        flow {
            val groupId = params.groupId
            val challengeStatus = params.challengeStatus
            val challenges = localLoadManager.fetchChallengesByStatusAndId(groupId, challengeStatus)
                .takeIf { it.isNotEmpty() }
                ?: remoteLoadManager.fetchChallengesByStatusAndId(groupId, challengeStatus)
            emit(challenges.toMutableList())
        }
}