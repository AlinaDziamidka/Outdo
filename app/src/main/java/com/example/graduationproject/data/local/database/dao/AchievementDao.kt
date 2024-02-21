package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.AchievementModel

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievements")
    fun fetchAll(): List<AchievementModel>

    @Query("SELECT * FROM achievements WHERE achievement_id = :achievementId LIMIT 1")
    fun fetchById(achievementId: Long): AchievementModel

    @Insert
    fun insertOne(achievementModel: AchievementModel)

    @Query("DELETE FROM achievements WHERE achievement_id = :achievementId")
    fun deleteById(achievementId: Long)

    @Update
    fun updateOne(achievementModel: AchievementModel)

}