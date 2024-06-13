package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.UserFriendModel

@Dao
interface UserFriendDao {

    @Query("SELECT * FROM user_friends")
    fun fetchAll(): List<UserFriendModel>

    @Query("SELECT * FROM user_friends WHERE  user_id = :userId")
    fun fetchFriendsByUserId(userId: String): List<UserFriendModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(userFriendModel: UserFriendModel)

    @Delete
    fun deleteOne(userFriendModel: UserFriendModel)

    @Update
    fun updateOne(userFriendModel: UserFriendModel)
}