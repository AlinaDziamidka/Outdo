package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("user_groups", primaryKeys = ["user_id", "group_id"])
data class UserGroupModel(
    @ColumnInfo("user_id")
    val userId: Long,
    @ColumnInfo("group_id")
    val groupId: Long
)
