package com.example.graduationproject.domain.util

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile

interface LoadManager {

    suspend fun fetchGroupsByUserId(userId: String): List<Group>

    suspend fun fetchUsersByGroupId(groupId: String): List<UserProfile>

    suspend fun fetchUserChallengesByGroupId(groupId: String): List<Challenge>

}