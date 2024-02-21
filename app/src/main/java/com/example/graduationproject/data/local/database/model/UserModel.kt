package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user")
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("user_id")
    val userId: Long,
    val login: String,
    @ColumnInfo(name = "nic_name")
    val nicName: String,
    @ColumnInfo(name = "user_avatar")
    val userAvatarPath: String
)
