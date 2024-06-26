package com.example.graduationproject.data.local.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.UserNotificationModel

interface UserNotificationDao {

    @Query("SELECT * FROM user_notifications")
    fun fetchAll(): List<UserNotificationModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(userNotificationModel: UserNotificationModel)

    @Delete
    fun deleteOne(userNotificationModel: UserNotificationModel)

    @Update
    fun updateOne(userNotificationModel: UserNotificationModel)
}