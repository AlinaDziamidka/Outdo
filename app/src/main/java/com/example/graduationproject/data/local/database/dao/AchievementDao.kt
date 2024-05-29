package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.AchievementModel
import com.example.graduationproject.data.local.database.model.ChallengeModel
import com.example.graduationproject.domain.util.Event

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievements")
    fun fetchAll(): List<AchievementModel>

    @Query("SELECT * FROM achievements WHERE id = :achievementId LIMIT 1")
    fun fetchById(achievementId: String): AchievementModel

    @Query("SELECT * FROM achievements WHERE achievementType = :achievementType LIMIT 1")
    fun fetchDailyAchievement(achievementType: String): AchievementModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(achievementModel: AchievementModel)

    @Query("DELETE FROM achievements WHERE id = :achievementId")
    fun deleteById(achievementId: String)

    @Update
    fun updateOne(achievementModel: AchievementModel)

}