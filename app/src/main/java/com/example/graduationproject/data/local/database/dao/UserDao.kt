package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.UserModel

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun fetchAll(): List<UserModel>

    @Query("SELECT * FROM users WHERE user_id = :userId LIMIT 1")
    fun fetchById(userId: String): UserModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOne(userModel: UserModel)

    @Query("DELETE FROM users WHERE user_id = :userId")
    fun deleteById(userId: String)

    @Update
    fun updateOne(userModel: UserModel)
}