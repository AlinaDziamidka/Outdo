package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("groups")
data class GroupModel(
    @PrimaryKey
    @ColumnInfo("id")
    val groupId: String,
    @ColumnInfo("name")
    val groupName: String,
    @ColumnInfo("user_creator_id")
    val creatorId: String,
    @ColumnInfo("avatar")
    val groupAvatarPath: String
)