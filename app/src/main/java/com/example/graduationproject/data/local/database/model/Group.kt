package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.nio.file.Path

@Entity
data class Group (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("group_name")
    val groupName : String,
    @ColumnInfo("user_creator_id")
    val creatorId : Int,
    @ColumnInfo("user_creator_id")
    val groupAvatar : Path
)