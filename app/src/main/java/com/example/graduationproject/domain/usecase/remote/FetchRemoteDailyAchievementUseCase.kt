package com.example.graduationproject.domain.usecase.remote

import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.repository.local.AchievementLocalRepository
import com.example.graduationproject.domain.repository.remote.AchievementRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import com.example.graduationproject.domain.util.writeToLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchRemoteDailyAchievementUseCase @Inject constructor(
    private val achievementRemoteRepository: AchievementRemoteRepository,
    private val achievementLocalRepository: AchievementLocalRepository
) : UseCase<FetchRemoteDailyAchievementUseCase.Params, Achievement> {

    data class Params(
        val achievementType: AchievementType,
    )

    override suspend fun invoke(params: Params): Flow<Achievement> =
        flow {
            val achievementTypeQuery = params.achievementType
            val achievement = getDailyAchievement(achievementTypeQuery)
            emit(achievement)
        }

    private suspend fun getDailyAchievement(achievementType: AchievementType): Achievement =
        withContext(Dispatchers.IO) {
            val event = achievementRemoteRepository.fetchDailyAchievement(achievementType.stringValue)
            when (event) {
                is Event.Success -> {
                    writeToLocalDatabase(achievementLocalRepository::insertOne, event.data)
                    event.data
                }

                is Event.Failure -> throw Exception(event.exception)
            }
        }
}