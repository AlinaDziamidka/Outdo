package com.example.graduationproject.data.remote.transormer

import com.example.graduationproject.data.remote.api.response.CompetitionResponse
import com.example.graduationproject.domain.entity.Competition

public class CompetitionTransformer {

    fun fromResponse(response: CompetitionResponse): Competition {
        return Competition(
            competitionId = response.competitionId,
            competitionName = response.competitionName,
            creatorId = response.creatorId,
            competitionAvatarPath = response.competitionAvatarPath,
        )
    }
}