package com.example.graduationproject.domain.entity

data class UserProfile(
    val userId: String,
    val username: String,
    val userEmail: String,
    val userAvatarPath: String?
)