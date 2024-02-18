package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.nio.file.Path

@Entity("groups")
data class Group (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("group_id")
    val groupId:Long,
    @ColumnInfo("group_name")
    val groupName : String,
    @ColumnInfo("user_creator_id")
    val creatorId : Long,
    @ColumnInfo("group_avatar")
    val groupAvatar : Path
)