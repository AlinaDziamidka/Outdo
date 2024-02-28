package com.example.graduationproject.data.remote.api

import com.example.graduationproject.data.remote.api.response.CategoryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryApiService {
    @GET("categories/{category_id}")
    suspend fun fetchCategory(@Path("category_id") categoryId: Long): CategoryResponse
}