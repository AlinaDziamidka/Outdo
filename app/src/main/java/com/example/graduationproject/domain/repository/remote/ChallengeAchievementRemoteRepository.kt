package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeAchievement
import com.example.graduationproject.domain.util.Event
import kotlinx.coroutines.flow.Flow

interface ChallengeAchievementRemoteRepository {

    suspend fun fetchAllAchievementsByChallengeId(challengeIdQuery: String): Event<List<Achievement>>
}