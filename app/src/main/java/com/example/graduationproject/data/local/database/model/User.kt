package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.nio.file.Path

@Entity("User")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val login: String,
    @ColumnInfo(name = "nic_name")
    val nicName: String,
    @ColumnInfo(name = "user_avatar")
    val userAvatar: Path
)
