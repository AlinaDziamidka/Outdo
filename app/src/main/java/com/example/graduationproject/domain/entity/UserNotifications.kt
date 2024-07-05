package com.example.graduationproject.domain.entity

import com.google.gson.annotations.SerializedName

data class UserNotifications(
    val userId: String,
    val creatorId: String,
    val groupId: String,
    val created: Long
)
