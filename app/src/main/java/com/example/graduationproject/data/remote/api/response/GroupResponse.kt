package com.example.graduationproject.data.remote.api.response

data class GroupResponse (
    val groupId: Long,
    val groupName: String,
    val creatorId: Long,
    val groupAvatarPath: String
)