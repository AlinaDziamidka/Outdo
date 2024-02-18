package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("group_challenges", primaryKeys = ["group_id", "challenge_id"])
data class GroupChallenge(
    @ColumnInfo("group_id")
    val groupId: Long,
    @ColumnInfo("challenge_id")
    val challengeId: Long
)