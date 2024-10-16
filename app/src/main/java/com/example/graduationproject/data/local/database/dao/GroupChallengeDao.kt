package com.example.graduationproject.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.GroupChallengeModel

@Dao
interface GroupChallengeDao {
    @Query("SELECT * FROM group_challenges")
    fun fetchAll(): List<GroupChallengeModel>

    @Query("SELECT * FROM group_challenges WHERE challenge_id = :challengeId")
    fun fetchGroupsByChallengeId(challengeId: String): List<GroupChallengeModel>

    @Query("SELECT * FROM group_challenges WHERE  group_id = :groupId")
    fun fetchChallengesByGroupId(groupId: String): List<GroupChallengeModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(groupChallengeModel: GroupChallengeModel)

    @Delete
    fun deleteOne(groupChallengeModel: GroupChallengeModel)

    @Update
    fun updateOne(groupChallengeModel: GroupChallengeModel)
}