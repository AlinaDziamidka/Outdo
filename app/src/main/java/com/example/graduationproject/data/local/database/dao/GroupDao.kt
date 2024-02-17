package com.example.graduationproject.data.local.database.dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.Group

interface GroupDao {

    @Query("SELECT * FROM `group`")
    fun fetchAll(): List<Group>

    @Query("SELECT * FROM `group` WHERE id = :id LIMIT 1")
    fun fetchById(id: Int): Group

    @Insert
    fun insertOne(group: Group)

    @Query("DELETE FROM `group` WHERE id = :id")
    fun deleteById(id: Int)

    @Update
    fun updateOne(group: Group)
}