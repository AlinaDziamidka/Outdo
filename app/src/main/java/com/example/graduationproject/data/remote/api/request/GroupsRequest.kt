package com.example.graduationproject.data.remote.api.request

data class GroupsRequest (
    val groupId: Long,
    val groupName: String,
    val creatorId: Long,
    val groupAvatarPath: String
)