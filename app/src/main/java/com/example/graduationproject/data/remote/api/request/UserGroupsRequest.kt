package com.example.graduationproject.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class UserGroupsRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("groupId")
    val groupId: String
)
