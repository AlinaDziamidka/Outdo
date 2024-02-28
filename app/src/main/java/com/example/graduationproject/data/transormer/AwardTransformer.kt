package com.example.graduationproject.data.transormer

import com.example.graduationproject.data.remote.api.response.AwardResponse
import com.example.graduationproject.domain.entity.Award

class AwardTransformer {

    fun fromResponse(response: AwardResponse): Award {
        return Award(
            awardId = response.awardId,
            awardName = response.awardName,
            awardIconPath = response.awardIconPath
        )
    }
}