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

    @Query("SELECT * FROM user WHERE user_id = :userId LIMIT 1")
    fun fetchById(userId: Long): User

    @Insert
    fun insertOne(user: User)

    @Query("DELETE FROM user WHERE user_id = :userId")
    fun deleteById(userId: Long)

    @Update
    fun updateOne(user: User)
}