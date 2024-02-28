package com.example.graduationproject.domain.repository

import com.example.graduationproject.domain.entity.Category

interface CategoryRepository {
    suspend fun fetchCategory(categoryId: Long): Category
}