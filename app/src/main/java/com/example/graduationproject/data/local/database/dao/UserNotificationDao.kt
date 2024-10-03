package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.UserNotificationModel

@Dao
interface UserNotificationDao {

    @Query("SELECT * FROM user_notifications")
    fun fetchAll(): List<UserNotificationModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(userNotificationModel: UserNotificationModel)

    @Query("DELETE FROM user_notifications WHERE creator_id = :creatorId AND group_id = :groupId")
    fun deleteById(creatorId: String, groupId: String)

    @Update
    fun updateOne(userNotificationModel: UserNotificationModel)
}