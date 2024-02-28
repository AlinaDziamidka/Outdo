package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.CategoryApiService
import com.example.graduationproject.data.transormer.CategoryTransformer
import com.example.graduationproject.domain.entity.Category
import com.example.graduationproject.domain.repository.CategoryRepository

class CategoryRepositoryImpl(private val categoryApiService: CategoryApiService) :
    CategoryRepository {

    private val categoryTransformer = CategoryTransformer()

    override suspend fun fetchCategory(categoryId: Long): Category {
        val response = categoryApiService.fetchCategory(categoryId)
        return categoryTransformer.fromResponse(response)
    }
}