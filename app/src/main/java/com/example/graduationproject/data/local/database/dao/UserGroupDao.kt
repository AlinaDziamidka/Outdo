package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.UserGroup

@Dao
interface UserGroupDao {

    @Query("SELECT * FROM user_groups")
    fun fetchAll(): List<UserGroup>

    @Query("SELECT * FROM user_groups WHERE  user_id = :userId")
    fun fetchGroupsByUserId(userId: Long): List<UserGroup>

    @Query("SELECT * FROM user_groups WHERE  group_id = :groupId")
    fun fetchUsersByGroupId(groupId: Long): List<UserGroup>

    @Insert
    fun insertOne(userGroup: UserGroup)

    @Delete
    fun deleteOne(userGroup: UserGroup)

    @Update
    fun updateOne(userGroup: UserGroup)
}