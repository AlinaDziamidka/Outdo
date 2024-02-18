package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.UserAward

@Dao
interface UserAwardDao {

    @Query("SELECT * FROM user_awards")
    fun fetchAll(): List<UserAward>

    @Query("SELECT * FROM user_awards WHERE  user_id = :userId")
    fun fetchAwardsByUserId(userId: Long): List<UserAward>

    @Query("SELECT * FROM user_awards WHERE  award_id = :awardId")
    fun fetchUsersByAwardId(awardId: Long): List<UserAward>

    @Insert
    fun insertOne(userAward: UserAward)

    @Delete
    fun deleteOne(userAward: UserAward)

    @Update
    fun updateOne(userAward: UserAward)
}