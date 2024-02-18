package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.Award

@Dao
interface AwardDao {

    @Query("SELECT * FROM awards")
    fun fetchAll(): List<Award>

    @Query("SELECT * FROM awards WHERE award_id = :awardId LIMIT 1")
    fun fetchById(awardId: Long): Award

    @Insert
    fun insertOne(award: Award)

    @Query("DELETE FROM awards WHERE award_id = :awardId")
    fun deleteById(awardId: Long)

    @Update
    fun updateOne(award: Award)
}