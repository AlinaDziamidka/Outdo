package com.example.graduationproject.data.remote.api.request

data class UserRequest(
    val userId: String,
    val username: String,
    val userIdentity: String,
    val userPassword: String,
    val userAvatarPath: String?
)
