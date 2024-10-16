package com.example.graduationproject.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("categories")
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("category_id")
    val categoryId: Long,
    @ColumnInfo("category_name")
    val categoryName: String,
    @ColumnInfo("category_icon")
    val awardIconPath: String
)