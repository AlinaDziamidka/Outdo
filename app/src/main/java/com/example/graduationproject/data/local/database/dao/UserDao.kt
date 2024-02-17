package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun fetchAll(): List<User>

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    fun fetchById(id: Int): User

    @Insert
    fun insertOne(user: User)

    @Query("DELETE FROM user WHERE id = :id")
    fun deleteById(id: Int)

    @Update
    fun updateOne(user: User)
}