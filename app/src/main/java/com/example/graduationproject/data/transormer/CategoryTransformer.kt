package com.example.graduationproject.data.transormer

import com.example.graduationproject.data.remote.api.response.CategoryResponse
import com.example.graduationproject.domain.entity.Category

class CategoryTransformer {
    fun fromResponse(response: CategoryResponse): Category {
        return Category(
            categoryId = response.categoryId,
            categoryName = response.categoryName,
            awardIconPath = response.awardIconPath
        )
    }
}