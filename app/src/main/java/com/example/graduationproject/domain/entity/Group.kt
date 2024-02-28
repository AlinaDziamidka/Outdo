package com.example.graduationproject.domain.entity

data class Group(
    val groupId: Long,
    val groupName: String,
    val creatorId: Long,
    val groupAvatarPath: String
)
