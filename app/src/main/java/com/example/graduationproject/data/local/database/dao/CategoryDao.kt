package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun fetchAll(): List<Category>

    @Query("SELECT * FROM categories WHERE category_id = :categoryId LIMIT 1")
    fun fetchById(categoryId: Long): Category

    @Insert
    fun insertOne(category: Category)

    @Query("DELETE FROM categories WHERE category_id = :categoryId")
    fun deleteById(categoryId: Long)

    @Update
    fun updateOne(category: Category)
}