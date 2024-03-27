package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.Award

interface AwardRepository {
    suspend fun fetchUserAward(awardId: Long): Award
}