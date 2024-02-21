package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.CompetitionModel

@Dao
interface CompetitionDao {

    @Query("SELECT * FROM competitions")
    fun fetchAll(): List<CompetitionModel>

    @Query("SELECT * FROM competitions WHERE competition_id = :competitionId LIMIT 1")
    fun fetchById(competitionId: Long): CompetitionModel

    @Insert
    fun insertOne(competitionModel: CompetitionModel)

    @Query("DELETE FROM competitions WHERE competition_id = :competitionId")
    fun deleteById(competitionId: Long)

    @Update
    fun updateOne(competitionModel: CompetitionModel)
}