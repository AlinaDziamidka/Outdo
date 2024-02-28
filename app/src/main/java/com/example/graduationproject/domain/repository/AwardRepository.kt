package com.example.graduationproject.domain.repository

import com.example.graduationproject.domain.entity.Award

interface AwardRepository {
    suspend fun fetchUserAward(awardId: Long): Award
}