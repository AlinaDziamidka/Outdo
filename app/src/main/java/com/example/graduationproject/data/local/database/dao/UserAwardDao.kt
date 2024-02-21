package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.UserAwardModel

@Dao
interface UserAwardDao {

    @Query("SELECT * FROM user_awards")
    fun fetchAll(): List<UserAwardModel>

    @Query("SELECT * FROM user_awards WHERE  user_id = :userId")
    fun fetchAwardsByUserId(userId: Long): List<UserAwardModel>

    @Query("SELECT * FROM user_awards WHERE  award_id = :awardId")
    fun fetchUsersByAwardId(awardId: Long): List<UserAwardModel>

    @Insert
    fun insertOne(userAwardModel: UserAwardModel)

    @Delete
    fun deleteOne(userAwardModel: UserAwardModel)

    @Update
    fun updateOne(userAwardModel: UserAwardModel)
}