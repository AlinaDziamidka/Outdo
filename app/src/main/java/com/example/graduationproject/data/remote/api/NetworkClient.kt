package com.example.graduationproject.data.remote.api

import com.example.graduationproject.data.remote.api.service.AchievementApiService
import com.example.graduationproject.data.remote.api.service.AwardApiService
import com.example.graduationproject.data.remote.api.service.CategoryApiService
import com.example.graduationproject.data.remote.api.service.ChallengeAchievementApiService
import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.api.service.CompetitionApiService
import com.example.graduationproject.data.remote.api.service.GroupApiService
import com.example.graduationproject.data.remote.api.service.GroupChallengeApiService
import com.example.graduationproject.data.remote.api.service.UserAwardApiService
import com.example.graduationproject.data.remote.api.service.UserCompetitionApiService
import com.example.graduationproject.data.remote.api.service.UserGroupApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {


    private const val BASE_URL = "https://goodlycattle.backendless.app/api/data/"

    private val logging = HttpLoggingInterceptor()

    init {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

    private var retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient).build()

    fun provideAchievementApiService(): AchievementApiService =
        retrofit.create(AchievementApiService::class.java)

    fun provideAwardApiService(): AwardApiService = retrofit.create(AwardApiService::class.java)

    fun provideCategoryApiService(): CategoryApiService =
        retrofit.create(CategoryApiService::class.java)

    fun provideChallengeAchievementApiService(): ChallengeAchievementApiService =
        retrofit.create(ChallengeAchievementApiService::class.java)

    fun provideChallengeApiService(): ChallengeApiService =
        retrofit.create(ChallengeApiService::class.java)

    fun provideCompetitionApiService(): CompetitionApiService =
        retrofit.create(CompetitionApiService::class.java)

    fun provideGroupApiService(): GroupApiService = retrofit.create(GroupApiService::class.java)

    fun provideGroupChallengeApiService(): GroupChallengeApiService =
        retrofit.create(GroupChallengeApiService::class.java)

    fun provideUserAwardApiService(): UserAwardApiService =
        retrofit.create(UserAwardApiService::class.java)

    fun provideUserCompetitionApiService(): UserCompetitionApiService =
        retrofit.create(UserCompetitionApiService::class.java)

    fun provideUserGroupApiService(): UserGroupApiService =
        retrofit.create(UserGroupApiService::class.java)
}

interface ApiCallback<T> {
    fun onSuccess(data: T)
    fun onFailure(message: String)
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val message: String) : Result<Nothing>()
}
