package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("user_notifications", primaryKeys = ["user_id", "group_id"])
data class UserNotificationModel(
    @ColumnInfo("userId")
    val userId: String,
    @ColumnInfo("groupId")
    val groupId: String,
    @ColumnInfo("created")
    val created: Long
)
