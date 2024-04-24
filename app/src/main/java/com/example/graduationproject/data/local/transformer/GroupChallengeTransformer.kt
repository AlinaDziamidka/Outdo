package com.example.graduationproject.data.local.transformer

import com.example.graduationproject.data.local.database.model.GroupChallengeModel
import com.example.graduationproject.domain.entity.GroupChallenge

public class GroupChallengeTransformer {

    fun fromModel(model: GroupChallengeModel): GroupChallenge {
        return GroupChallenge(
            groupId = model.groupId,
            challengeId = model.challengeId
        )
    }

    fun toModel(groupChallenge: GroupChallenge): GroupChallengeModel {
        return GroupChallengeModel(
            groupId = groupChallenge.groupId,
            challengeId = groupChallenge.challengeId
        )
    }
}