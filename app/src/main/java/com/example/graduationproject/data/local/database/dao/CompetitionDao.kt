package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.Competition
import com.example.graduationproject.data.local.database.model.Group

@Dao
interface CompetitionDao {

    @Query("SELECT * FROM competitions")
    fun fetchAll(): List<Competition>

    @Query("SELECT * FROM competitions WHERE competition_id = :competitionId LIMIT 1")
    fun fetchById(competitionId: Long): Competition

    @Insert
    fun insertOne(competition: Competition)

    @Query("DELETE FROM competitions WHERE competition_id = :competitionId")
    fun deleteById(competitionId: Long)

    @Update
    fun updateOne(competition: Competition)
}