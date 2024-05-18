package com.example.graduationproject.data.remote.transormer


import com.example.graduationproject.data.remote.api.response.UserResponse
import com.example.graduationproject.domain.entity.UserProfile

public class UserTransformer {
    fun fromResponse(response: UserResponse): UserProfile {
        return UserProfile(
            userId = response.userId,
            username = response.username,
            userEmail = response.userIdentity,
            userAvatarPath = response.userAvatarPath
        )
    }
}