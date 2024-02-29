package com.example.graduationproject.domain.usecase

import com.example.graduationproject.data.repository.AchievementRepositoryImpl
import com.example.graduationproject.data.repository.ChallengeAchievementRepositoryImpl
import com.example.graduationproject.domain.entity.Achievement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow

class FetchChallengeAchievementsUseCase(
    private val challengeAchievementRepository: ChallengeAchievementRepositoryImpl,
    private val achievementRepository: AchievementRepositoryImpl
) {

    suspend operator fun invoke(challengeId: Long): Flow<List<Achievement>> {
        val challengeAchievements =
            challengeAchievementRepository.fetchAllAchievementsByChallengeId(challengeId)
        return challengeAchievements.flatMapConcat { challengeAchievement ->
            flow {
                val achievements = mutableListOf<Achievement>()
                for (achievement in challengeAchievement) {
                    val fetchedAchievement =
                        achievementRepository.fetchAchievement(achievement.achievementId)
                    achievements.add(fetchedAchievement)
                }
                emit(achievements)
            }
        }
    }

}