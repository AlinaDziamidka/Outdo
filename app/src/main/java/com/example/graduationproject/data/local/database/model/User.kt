package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.nio.file.Path

@Entity("user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("user_id")
    val userId: Long,
    val login: String,
    @ColumnInfo(name = "nic_name")
    val nicName: String,
    @ColumnInfo(name = "user_avatar")
    val userAvatar: Path
)
