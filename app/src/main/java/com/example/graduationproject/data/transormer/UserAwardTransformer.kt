package com.example.graduationproject.data.transormer

import com.example.graduationproject.data.remote.api.response.UserAwardResponse
import com.example.graduationproject.domain.entity.UserAward

class UserAwardTransformer {

    fun fromResponse(response: UserAwardResponse): UserAward {
        return UserAward(
            awardId = response.awardId,
            userId = response.userId
        )
    }
}