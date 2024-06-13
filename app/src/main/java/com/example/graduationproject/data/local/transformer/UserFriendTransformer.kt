package com.example.graduationproject.data.local.transformer

import com.example.graduationproject.data.local.database.model.UserFriendModel
import com.example.graduationproject.domain.entity.UserFriend

class UserFriendTransformer {

    fun fromModel(model: UserFriendModel): UserFriend {
        return UserFriend(
            userId = model.userId,
            friendId = model.friendId
        )
    }

    fun toModel(userFriend: UserFriend): UserFriendModel {
        return UserFriendModel(
            userId = userFriend.userId,
            friendId = userFriend.friendId
        )
    }
}