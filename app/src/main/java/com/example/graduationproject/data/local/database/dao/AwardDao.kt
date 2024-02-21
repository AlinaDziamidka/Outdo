package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.AwardModel

@Dao
interface AwardDao {

    @Query("SELECT * FROM awards")
    fun fetchAll(): List<AwardModel>

    @Query("SELECT * FROM awards WHERE award_id = :awardId LIMIT 1")
    fun fetchById(awardId: Long): AwardModel

    @Insert
    fun insertOne(awardModel: AwardModel)

    @Query("DELETE FROM awards WHERE award_id = :awardId")
    fun deleteById(awardId: Long)

    @Update
    fun updateOne(awardModel: AwardModel)
}