package com.example.graduationproject.domain.repository.local

import com.example.graduationproject.domain.entity.GroupChallenge

interface GroupChallengeLocalRepository {

    suspend fun fetchAll(): List<GroupChallenge>

    suspend fun fetchGroupsByChallengeId(challengeId: String): List<GroupChallenge>

    suspend fun fetchChallengesByGroupId(groupId: String): List<GroupChallenge>

    suspend fun insertOne(groupChallenge: GroupChallenge)

    suspend fun deleteOne(groupChallenge: GroupChallenge)

    suspend fun updateOne(groupChallenge: GroupChallenge)
}