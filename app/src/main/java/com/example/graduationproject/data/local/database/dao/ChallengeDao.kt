package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.ChallengeModel

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges")
    fun fetchAll(): List<ChallengeModel>

    @Query("SELECT * FROM challenges WHERE id = :challengeId LIMIT 1")
    fun fetchById(challengeId: String): ChallengeModel

    @Query("SELECT * FROM challenges WHERE challengeType = :challengeType LIMIT 1")
    fun fetchWeekChallenge(challengeType: String): ChallengeModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOne(challengeModel: ChallengeModel)

    @Query("DELETE FROM challenges WHERE id = :challengeId")
    fun deleteById(challengeId: String)

    @Update
    fun updateOne(challengeModel: ChallengeModel)
}