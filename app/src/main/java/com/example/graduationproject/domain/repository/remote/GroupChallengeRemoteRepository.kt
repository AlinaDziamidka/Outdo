package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.data.remote.api.request.GroupChallengeRequest
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.util.Event
import retrofit2.Response
import retrofit2.http.Body

interface GroupChallengeRemoteRepository {
    suspend fun fetchAllChallengesByGroupId(groupIdQuery: String): Event<List<Challenge>>

    suspend fun fetchChallengesByGroupIdANDStatus(
        groupIdQuery: String,
        challengeStatusQuery: String
    ): Event<List<Challenge>>

    suspend fun insertGroupChallenge(groupId: String, challengeId: String): Event<GroupChallenge>
}