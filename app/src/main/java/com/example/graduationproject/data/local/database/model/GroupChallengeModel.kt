package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("group_challenges", primaryKeys = ["group_id", "challenge_id"])
data class GroupChallengeModel(
    @ColumnInfo("group_id")
    val groupId: String,
    @ColumnInfo("challenge_id")
    val challengeId: String
)