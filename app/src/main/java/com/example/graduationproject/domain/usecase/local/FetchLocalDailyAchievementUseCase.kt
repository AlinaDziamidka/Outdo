package com.example.graduationproject.domain.usecase.local

import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.repository.local.AchievementLocalRepository
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchLocalDailyAchievementUseCase @Inject constructor(
    private val achievementLocalRepository: AchievementLocalRepository
) : UseCase<FetchLocalDailyAchievementUseCase.Params, Achievement> {

    data class Params(
        val achievementType: AchievementType,
    )

    override suspend fun invoke(params: Params): Flow<Achievement> =
        flow {
            val achievementTypeQuery = params.achievementType
            val achievement = withContext(Dispatchers.IO) {
                achievementLocalRepository.fetchDailyAchievement(achievementTypeQuery.stringValue)
            }
            emit(achievement)
        }
}