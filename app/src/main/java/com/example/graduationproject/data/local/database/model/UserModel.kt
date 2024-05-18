package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("users")
data class UserModel(
    @PrimaryKey()
    @ColumnInfo("user_id")
    val userId: String,
    @ColumnInfo("email")
    val userEmail: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "user_avatar")
    val userAvatarPath: String?
)
