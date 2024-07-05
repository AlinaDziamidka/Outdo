package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.request.UserNotificationsRequest
import com.example.graduationproject.data.remote.api.response.UserNotificationsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface UserNotificationsApiService {

    @POST("data/userNotifications")
    suspend fun insertNotification(@Body request: UserNotificationsRequest): Response<UserNotificationsResponse>

    @GET("data/userNotifications")
    suspend fun fetchNotificationsByUserId(@Query("where") userIdQuery: String): Response<List<UserNotificationsResponse>>

    @DELETE("data/bulk/userNotifications")
    suspend fun deleteNotification(
        @Query("where") query: String,
    ): Response<Long>
}