package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.UserAchievementModel

@Dao
interface UserAchievementDao {

    @Query("SELECT * FROM user_achievements")
    fun fetchAll(): List<UserAchievementModel>

    @Query("SELECT * FROM user_achievements WHERE  user_id = :userId")
    fun fetchAchievementsByUserId(userId: String): List<UserAchievementModel>

    @Query("SELECT * FROM user_achievements WHERE  achievement_id = :achievementId")
    fun fetchUsersByAchievementId(achievementId: String): List<UserAchievementModel>

    @Query("SELECT * FROM user_achievements WHERE  achievement_id = :achievementId AND user_id = :userId")
    fun fetchByUserIdAndAchievementId(achievementId: String, userId: String): UserAchievementModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(userAchievementModel: UserAchievementModel)

    @Delete
    fun deleteOne(userAchievementModel: UserAchievementModel)

    @Update
    fun updateOne(userAchievementModel: UserAchievementModel)

    @Query("UPDATE user_achievements SET achievement_status = :achievementStatus, photo = :photoUrl WHERE achievement_id = :achievementId AND user_id = :userId")
    fun updatePhoto(
        achievementStatus: String,
        photoUrl: String?,
        achievementId: String,
        userId: String
    )
}