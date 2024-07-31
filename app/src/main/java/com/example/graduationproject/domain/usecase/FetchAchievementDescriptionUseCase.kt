package com.example.graduationproject.domain.usecase

import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.repository.local.AchievementLocalRepository
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchAchievementDescriptionUseCase @Inject
constructor(
    private val achievementLocalRepository: AchievementLocalRepository
) : UseCase<FetchAchievementDescriptionUseCase.Params, Achievement> {

    data class Params(
        val achievementId: String
    )

    override suspend fun invoke(params: Params): Flow<Achievement> =
        flow {
            val challenge = achievementLocalRepository.fetchById(params.achievementId)
            emit(challenge)
        }.flowOn(Dispatchers.IO)
}