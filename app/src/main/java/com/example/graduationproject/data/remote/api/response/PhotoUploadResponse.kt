package com.example.graduationproject.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class PhotoUploadResponse (
    @SerializedName("fileURL")
    val fileUrl: String
)