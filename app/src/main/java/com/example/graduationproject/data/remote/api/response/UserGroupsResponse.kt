package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class UserGroupsResponse(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("groupId")
    val groupId: String
)
