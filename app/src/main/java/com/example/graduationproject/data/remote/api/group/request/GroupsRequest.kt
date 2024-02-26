package com.example.graduationproject.data.remote.api.group.request

data class GroupsRequest (
    val groupId: Long,
    val groupName: String,
    val creatorId: Long,
    val groupAvatarPath: String
)