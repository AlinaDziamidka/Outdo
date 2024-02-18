package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.UserCompetition

@Dao
interface UserCompetitionDao {

    @Query("SELECT * FROM user_competitions")
    fun fetchAll(): List<UserCompetition>

    @Query("SELECT * FROM user_competitions WHERE  user_id = :userId")
    fun fetchCompetitionByUserId(userId: Long): List<UserCompetition>

    @Query("SELECT * FROM user_competitions WHERE  competition_id = :competitionId")
    fun fetchUsersByCompetitionId(competitionId: Long): List<UserCompetition>

    @Insert
    fun insertOne(userCompetition: UserCompetition)

    @Delete
    fun deleteOne(userCompetition: UserCompetition)

    @Update
    fun updateOne(userCompetition: UserCompetition)

}