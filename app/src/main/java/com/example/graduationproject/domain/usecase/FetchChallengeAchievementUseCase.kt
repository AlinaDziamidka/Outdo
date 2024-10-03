package com.example.graduationproject.domain.usecase

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.util.LoadManager
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchChallengeAchievementUseCase @Inject constructor(
    @Remote private val remoteLoadManager: LoadManager,
    @Local private val localLoadManager: LoadManager
) : UseCase<FetchChallengeAchievementUseCase.Params, MutableList<Achievement>> {

    data class Params(
        val challengeId: String
    )

    override suspend fun invoke(params: Params): Flow<MutableList<Achievement>> =
        flow {
            val challengeId = params.challengeId
            val achievements = localLoadManager.fetchAchievementsByChallengeId(challengeId)
                .takeIf { it.isNotEmpty() }
                ?: remoteLoadManager.fetchAchievementsByChallengeId(challengeId)
            emit(achievements.toMutableList())
        }
}