package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.UserFriendsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserFriendApiService {
    @GET("data/userFriends")
    suspend fun fetchFriendsByUserId(@Query("where") userIdQuery: String): Response<List<UserFriendsResponse>>
}