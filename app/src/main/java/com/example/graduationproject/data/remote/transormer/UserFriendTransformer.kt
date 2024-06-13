package com.example.graduationproject.data.remote.transormer

import com.example.graduationproject.data.remote.api.response.UserFriendsResponse
import com.example.graduationproject.domain.entity.UserFriend

class UserFriendTransformer {

    fun fromResponse(response: UserFriendsResponse): UserFriend {
        return UserFriend(
            userId = response.userId,
            friendId = response.friendId
        )
    }
}