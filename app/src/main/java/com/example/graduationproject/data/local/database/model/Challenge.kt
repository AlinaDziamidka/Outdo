package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Challenge(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo("challenge_name")
    val challengeName: String,
    @ColumnInfo("category_id")
    val categoryId: Int,
    @ColumnInfo("challenge_type")
    val challengeType : Enum
)
