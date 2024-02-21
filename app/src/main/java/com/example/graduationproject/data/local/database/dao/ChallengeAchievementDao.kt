package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.ChallengeAchievementModel

@Dao
interface ChallengeAchievementDao {

    @Query("SELECT * FROM challenge_achievements")
    fun fetchAll(): List<ChallengeAchievementModel>

    @Query("SELECT * FROM challenge_achievements WHERE challenge_id = :challengeId")
    fun fetchAchievementsByChallengeId(challengeId: Long): List<ChallengeAchievementModel>

    @Query("SELECT * FROM challenge_achievements WHERE  achievement_id = :achievementId")
    fun fetchChallengesByChallengeId(achievementId: Long): List<ChallengeAchievementModel>

    @Insert
    fun insertOne(challengeAchievementModel: ChallengeAchievementModel)

    @Delete
    fun deleteOne(challengeAchievementModel: ChallengeAchievementModel)

    @Update
    fun updateOne(challengeAchievementModel: ChallengeAchievementModel)
}