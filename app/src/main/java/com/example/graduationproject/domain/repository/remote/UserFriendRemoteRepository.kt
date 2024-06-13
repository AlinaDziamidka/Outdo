package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.entity.UserFriend
import com.example.graduationproject.domain.util.Event

interface UserFriendRemoteRepository {

    suspend fun fetchFriendsByUserId(userIdQuery: String): Event<List<UserFriend>>
}