package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user")
data class UserModel(
    @PrimaryKey()
    @ColumnInfo("user_id")
    val userId: String,
    @ColumnInfo("user_identity")
    val userIdentity: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "user_avatar")
    val userAvatarPath: String
)
