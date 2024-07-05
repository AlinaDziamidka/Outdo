package com.example.graduationproject.data.local.transformer

import android.util.Log
import com.example.graduationproject.data.local.database.model.UserModel
import com.example.graduationproject.domain.entity.UserProfile

class UserTransformer {

    fun fromModel(model: UserModel): UserProfile {
        return UserProfile(
            userId = model.userId,
            userEmail = model.userEmail,
            username = model.username,
            userAvatarPath = model.userAvatarPath
        )
    }

    fun toModel(user: UserProfile): UserModel {
        return UserModel(
            userId = user.userId,
            userEmail = user.userEmail,
            username = user.username,
            userAvatarPath = user.userAvatarPath
        )
    }
}