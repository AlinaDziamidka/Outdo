package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.work.impl.SchedulersCreator

@Entity("user_notifications", primaryKeys = ["user_id", "group_id"])
data class UserNotificationModel(
//    @PrimaryKey
//    @ColumnInfo("objectId")
//    val objectId: String,
    @ColumnInfo("user_id")
    val userId: String,
    @ColumnInfo("creator_id")
    val creatorId: String,
    @ColumnInfo("group_id")
    val groupId: String,
    @ColumnInfo("created")
    val created: Long
)
