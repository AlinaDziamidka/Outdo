package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class GroupsRequest (
    @SerializedName("name")
    val groupName: String,
    @SerializedName("creatorId")
    val creatorId: String,
    @SerializedName("avatar")
    val groupAvatarPath: String?
)