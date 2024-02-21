package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.UserModel

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun fetchAll(): List<UserModel>

    @Query("SELECT * FROM user WHERE user_id = :userId LIMIT 1")
    fun fetchById(userId: Long): UserModel

    @Insert
    fun insertOne(userModel: UserModel)

    @Query("DELETE FROM user WHERE user_id = :userId")
    fun deleteById(userId: Long)

    @Update
    fun updateOne(userModel: UserModel)
}