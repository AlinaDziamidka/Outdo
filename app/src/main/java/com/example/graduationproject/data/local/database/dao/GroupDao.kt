package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.GroupModel

@Dao
interface GroupDao {

    @Query("SELECT * FROM groups")
    fun fetchAll(): List<GroupModel>

    @Query("SELECT * FROM groups WHERE id = :groupId LIMIT 1")
    fun fetchById(groupId: String): GroupModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(groupModel: GroupModel)

    @Query("DELETE FROM groups WHERE id = :groupId")
    fun deleteById(groupId: String)

    @Update
    fun updateOne(groupModel: GroupModel)
}