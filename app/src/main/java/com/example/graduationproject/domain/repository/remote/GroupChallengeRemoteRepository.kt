package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.util.Event

interface GroupChallengeRemoteRepository {
    suspend fun fetchAllChallengesByGroupId(groupIdQuery: String): Event<List<Challenge>>

    suspend fun fetchChallengesByGroupIdANDStatus(
        groupIdQuery: String,
        challengeStatusQuery: String
    ): Event<List<Challenge>>

    suspend fun insertGroupChallenge(groupId: String, challengeId: String): Event<GroupChallenge>
}