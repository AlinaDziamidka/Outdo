package com.example.graduationproject.data.remote.transormer

import com.example.graduationproject.data.remote.api.response.UserCompetitionResponse
import com.example.graduationproject.data.remote.api.response.UserGroupsResponse
import com.example.graduationproject.domain.entity.UserCompetition
import com.example.graduationproject.domain.entity.UserGroup

class UserCompetitionTransformer {

    fun fromResponse(response: UserCompetitionResponse): UserCompetition {
        return UserCompetition(
            competitionId = response.competitionId,
            userId = response.userId
        )
    }
}