package com.example.graduationproject.data.local.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.graduationproject.data.local.database.model.GroupChallenge

interface GroupChallengeDao {
    @Query("SELECT * FROM group_challenges")
    fun fetchAll(): List<GroupChallenge>

    @Query("SELECT * FROM group_challenges WHERE challenge_id = :challengeId")
    fun fetchGroupsByChallengeId(challengeId: Long): List<GroupChallenge>

    @Query("SELECT * FROM group_challenges WHERE  group_id = :groupId")
    fun fetchChallengesByGroupId(groupId: Long): List<GroupChallenge>

    @Insert
    fun insertOne(groupChallenge: GroupChallenge)

    @Delete
    fun deleteOne(groupChallenge: GroupChallenge)

    @Update
    fun updateOne(groupChallenge: GroupChallenge)
}