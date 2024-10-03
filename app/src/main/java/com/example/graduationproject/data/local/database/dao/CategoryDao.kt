package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.CategoryModel

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun fetchAll(): List<CategoryModel>

    @Query("SELECT * FROM categories WHERE category_id = :categoryId LIMIT 1")
    fun fetchById(categoryId: Long): CategoryModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(categoryModel: CategoryModel)

    @Query("DELETE FROM categories WHERE category_id = :categoryId")
    fun deleteById(categoryId: Long)

    @Update
    fun updateOne(categoryModel: CategoryModel)
}