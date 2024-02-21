package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.UserCompetitionModel

@Dao
interface UserCompetitionDao {

    @Query("SELECT * FROM user_competitions")
    fun fetchAll(): List<UserCompetitionModel>

    @Query("SELECT * FROM user_competitions WHERE  user_id = :userId")
    fun fetchCompetitionByUserId(userId: Long): List<UserCompetitionModel>

    @Query("SELECT * FROM user_competitions WHERE  competition_id = :competitionId")
    fun fetchUsersByCompetitionId(competitionId: Long): List<UserCompetitionModel>

    @Insert
    fun insertOne(userCompetitionModel: UserCompetitionModel)

    @Delete
    fun deleteOne(userCompetitionModel: UserCompetitionModel)

    @Update
    fun updateOne(userCompetitionModel: UserCompetitionModel)

}