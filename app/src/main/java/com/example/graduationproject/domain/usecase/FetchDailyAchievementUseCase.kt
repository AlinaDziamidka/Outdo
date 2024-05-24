package com.example.graduationproject.domain.usecase

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.LoadManager
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchDailyAchievementUseCase @Inject constructor(
    @Remote private val remoteLoadManager: LoadManager,
    @Local private val localLoadManager: LoadManager
) : UseCase<FetchDailyAchievementUseCase.Params, Achievement> {

    data class Params(
        val achievementType: AchievementType,
    )

    override suspend fun invoke(params: Params): Flow<Achievement> =
        flow {
            val achievementTypeQuery = params.achievementType
            var event = localLoadManager.fetchDailyAchievement(achievementTypeQuery)

            if (event is Event.Failure) {
                event = remoteLoadManager.fetchDailyAchievement(achievementTypeQuery)
            }

            when (event) {
                is Event.Success -> emit(event.data)
                is Event.Failure -> throw Exception(event.exception)
            }
        }
}