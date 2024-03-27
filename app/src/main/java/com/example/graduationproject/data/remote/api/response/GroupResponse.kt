package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class GroupResponse (
    @SerializedName("objectId")
    val groupId: String,
    @SerializedName("name")
    val groupName: String,
    @SerializedName("creatorId")
    val creatorId: String,
    @SerializedName("avatar")
    val groupAvatarPath: String
)