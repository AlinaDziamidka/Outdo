package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.Challenge
import com.example.graduationproject.data.local.database.model.Group

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges")
    fun fetchAll(): List<Challenge>

    @Query("SELECT * FROM challenges WHERE challenge_id = :challengeId LIMIT 1")
    fun fetchById(challengeId: Long): Challenge

    @Insert
    fun insertOne(challenge: Challenge)

    @Query("DELETE FROM challenges WHERE challenge_id = :challengeId")
    fun deleteById(challengeId: Long)

    @Update
    fun updateOne(challenge: Challenge)
}