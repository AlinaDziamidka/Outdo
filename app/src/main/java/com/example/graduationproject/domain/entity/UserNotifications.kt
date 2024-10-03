package com.example.graduationproject.domain.entity

data class UserNotifications(
    val userId: String,
    val creatorId: String,
    val groupId: String,
    val created: Long
)
