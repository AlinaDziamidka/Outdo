package com.example.graduationproject.domain.entity

data class User(
    val userId: Long,
    val username: String,
    val userIdentity: String,
    val userPassword: String,
    val userAvatarPath: String?
)
