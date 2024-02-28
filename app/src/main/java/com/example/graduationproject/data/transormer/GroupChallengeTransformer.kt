package com.example.graduationproject.data.transormer

import com.example.graduationproject.data.remote.api.response.GroupChallengeResponse
import com.example.graduationproject.domain.entity.GroupChallenge

class GroupChallengeTransformer {

    fun fromResponse(response: GroupChallengeResponse): GroupChallenge {
        return GroupChallenge(
            challengeId = response.challengeId,
            groupId = response.groupId
        )
    }
}