package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.UserFriendApiService
import com.example.graduationproject.data.remote.transormer.UserFriendTransformer
import com.example.graduationproject.domain.entity.UserFriend
import com.example.graduationproject.domain.repository.remote.UserFriendRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class UserFriendRemoteRepositoryImpl @Inject constructor(private val userFriendApiService: UserFriendApiService) :
    UserFriendRemoteRepository {

    override suspend fun fetchFriendsByUserId(userIdQuery: String): Event<List<UserFriend>> {
        val query = "userId=\'$userIdQuery\'"
        val event = doCall {
            return@doCall userFriendApiService.fetchFriendsByUserId(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val userFriends = response.map { userFriendsResponse ->
                    val userFriendTransformer = UserFriendTransformer()
                    userFriendTransformer.fromResponse(userFriendsResponse)
                }
                Event.Success(userFriends)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}