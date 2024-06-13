package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("user_friends", primaryKeys = ["user_id", "friend_id"])
data class UserFriendModel(
    @ColumnInfo("user_id")
    val userId: String,
    @ColumnInfo("friend_id")
    val friendId: String
)
