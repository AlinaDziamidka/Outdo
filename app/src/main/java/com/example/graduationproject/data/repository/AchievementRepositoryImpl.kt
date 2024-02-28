package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.AchievementApiService
import com.example.graduationproject.data.transormer.AchievementTransformer
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.Award
import com.example.graduationproject.domain.repository.AchievementRepository

class AchievementRepositoryImpl(private val achievementApiService: AchievementApiService) :
    AchievementRepository {

    private val achievementTransformer = AchievementTransformer()

    override suspend fun fetchAchievement(achievementId: Long): Achievement {
        val response = achievementApiService.fetchAchievement(achievementId)
        return achievementTransformer.fromResponse(response)
    }
}