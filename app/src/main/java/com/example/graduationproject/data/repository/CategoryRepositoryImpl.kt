package com.example.graduationproject.data.repository

import com.example.graduationproject.data.remote.api.service.CategoryApiService
import com.example.graduationproject.data.transormer.CategoryTransformer
import com.example.graduationproject.domain.entity.Category
import com.example.graduationproject.domain.repository.CategoryRepository

class CategoryRepositoryImpl(private val categoryApiService: CategoryApiService) :
    CategoryRepository {

    override suspend fun fetchCategory(categoryId: Long): Category {
        val response = categoryApiService.fetchCategory(categoryId)
        val categoryTransformer = CategoryTransformer()
        return categoryTransformer.fromResponse(response)
    }
}