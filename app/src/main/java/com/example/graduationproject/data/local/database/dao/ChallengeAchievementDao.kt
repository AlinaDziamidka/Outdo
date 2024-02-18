package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.ChallengeAchievement

@Dao
interface ChallengeAchievementDao {

    @Query("SELECT * FROM challenge_achievements")
    fun fetchAll(): List<ChallengeAchievement>

    @Query("SELECT * FROM challenge_achievements WHERE challenge_id = :challengeId")
    fun fetchAchievementsByChallengeId(challengeId: Long): List<ChallengeAchievement>

    @Query("SELECT * FROM challenge_achievements WHERE  achievement_id = :achievementId")
    fun fetchChallengesByChallengeId(achievementId: Long): List<ChallengeAchievement>

    @Insert
    fun insertOne(challengeAchievement: ChallengeAchievement)

    @Delete
    fun deleteOne(challengeAchievement: ChallengeAchievement)

    @Update
    fun updateOne(challengeAchievement: ChallengeAchievement)
}