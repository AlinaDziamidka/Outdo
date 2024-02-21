package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.UserGroupModel

@Dao
interface UserGroupDao {

    @Query("SELECT * FROM user_groups")
    fun fetchAll(): List<UserGroupModel>

    @Query("SELECT * FROM user_groups WHERE  user_id = :userId")
    fun fetchGroupsByUserId(userId: Long): List<UserGroupModel>

    @Query("SELECT * FROM user_groups WHERE  group_id = :groupId")
    fun fetchUsersByGroupId(groupId: Long): List<UserGroupModel>

    @Insert
    fun insertOne(userGroupModel: UserGroupModel)

    @Delete
    fun deleteOne(userGroupModel: UserGroupModel)

    @Update
    fun updateOne(userGroupModel: UserGroupModel)
}