package com.example.graduationproject.data.remote.api.request

data class CategoryRequest(
    val categoryId: Long,
    val categoryName: String,
    val awardIconPath: String
)