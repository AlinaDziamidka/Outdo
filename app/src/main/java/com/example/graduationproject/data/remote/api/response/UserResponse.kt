package com.example.graduationproject.data.remote.api.response

data class UserResponse(
    val userId: Long,
    val username: String,
    val userIdentity: String,
    val userPassword: String,
    val userAvatarPath: String?
)

