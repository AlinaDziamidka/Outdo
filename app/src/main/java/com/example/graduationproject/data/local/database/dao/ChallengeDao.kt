package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.ChallengeModel

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges")
    fun fetchAll(): List<ChallengeModel>

    @Query("SELECT * FROM challenges WHERE challenge_id = :challengeId LIMIT 1")
    fun fetchById(challengeId: Long): ChallengeModel

    @Insert
    fun insertOne(challengeModel: ChallengeModel)

    @Query("DELETE FROM challenges WHERE challenge_id = :challengeId")
    fun deleteById(challengeId: Long)

    @Update
    fun updateOne(challengeModel: ChallengeModel)
}