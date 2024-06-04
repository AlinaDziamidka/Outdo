package com.example.graduationproject.domain.util

import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile

interface LoadManager {

    suspend fun fetchGroupsByUserId(userId: String): List<Group>

    suspend fun fetchUsersByGroupId(groupId: String): List<UserProfile>

    suspend fun fetchUserChallengesByGroupId(groupId: String): List<Challenge>

    suspend fun fetchDailyAchievement(achievementType: AchievementType): Event<Achievement>

    suspend fun fetchWeekChallenge(challengeType: ChallengeType): Event<Challenge>

    suspend fun fetchChallengesByStatusAndId(
        groupId: String,
        challengeStatus: ChallengeStatus
    ): List<Challenge>

    suspend fun fetchAchievementsByChallengeId(challengeId: String): List<Achievement>

}