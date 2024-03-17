package com.example.graduationproject.domain.entity

data class UserProfile(
    val userId: String,
    val username: String,
    val userIdentity: String,
    val userAvatarPath: String?
)
