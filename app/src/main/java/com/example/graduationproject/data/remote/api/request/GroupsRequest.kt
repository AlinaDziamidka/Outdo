package com.example.graduationproject.data.remote.api.request

data class GroupsRequest (
    val groupId: String,
    val groupName: String,
    val creatorId: String,
    val groupAvatarPath: String
)