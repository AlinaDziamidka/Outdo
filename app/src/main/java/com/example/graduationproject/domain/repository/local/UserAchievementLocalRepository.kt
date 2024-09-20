package com.example.graduationproject.domain.repository.local
import com.example.graduationproject.domain.entity.UserAchievement

interface UserAchievementLocalRepository {

    suspend fun fetchAll(): List<UserAchievement>

    suspend fun fetchAchievementsByUserId(userId: String): List<UserAchievement>

    suspend fun fetchUsersByAchievementId(achievementId: String): List<UserAchievement>

    suspend fun fetchByUserIdAndAchievementId(achievementId: String, userId: String): UserAchievement?

    suspend fun insertOne(userAchievement: UserAchievement)

    suspend fun deleteOne(userAchievement: UserAchievement)

    suspend fun updateOne(userAchievement: UserAchievement)

    suspend fun updatePhoto(photoUrl: String?, achievementId: String, userId: String)
}