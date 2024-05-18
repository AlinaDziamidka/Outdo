package com.example.graduationproject.domain.entity

data class GroupParticipants(
    val group: Group,
    val users: List<UserProfile>
)
