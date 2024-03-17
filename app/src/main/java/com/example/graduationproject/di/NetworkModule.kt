package com.example.graduationproject.di


import com.example.graduationproject.data.remote.api.NetworkClientConfig
import com.example.graduationproject.data.remote.api.service.AchievementApiService
import com.example.graduationproject.data.remote.api.service.AuthApiService
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
import com.example.graduationproject.data.remote.api.service.UsernameApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkClientConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }


    @Provides
    fun provideHttpInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideAchievementApiService(retrofit: Retrofit): AchievementApiService =
        retrofit.create(AchievementApiService::class.java)

    @Provides
    fun provideAwardApiService(retrofit: Retrofit): AwardApiService =
        retrofit.create(AwardApiService::class.java)

    @Provides
    fun provideCategoryApiService(retrofit: Retrofit): CategoryApiService =
        retrofit.create(CategoryApiService::class.java)

    @Provides
    fun provideChallengeAchievementApiService(retrofit: Retrofit): ChallengeAchievementApiService =
        retrofit.create(ChallengeAchievementApiService::class.java)

    @Provides
    fun provideChallengeApiService(retrofit: Retrofit): ChallengeApiService =
        retrofit.create(ChallengeApiService::class.java)

    @Provides
    fun provideCompetitionApiService(retrofit: Retrofit): CompetitionApiService =
        retrofit.create(CompetitionApiService::class.java)

    @Provides
    fun provideGroupApiService(retrofit: Retrofit): GroupApiService =
        retrofit.create(GroupApiService::class.java)

    @Provides
    fun provideGroupChallengeApiService(retrofit: Retrofit): GroupChallengeApiService =
        retrofit.create(GroupChallengeApiService::class.java)

    @Provides
    fun provideUserAwardApiService(retrofit: Retrofit): UserAwardApiService =
        retrofit.create(UserAwardApiService::class.java)

    @Provides
    fun provideUserCompetitionApiService(retrofit: Retrofit): UserCompetitionApiService =
        retrofit.create(UserCompetitionApiService::class.java)

    @Provides
    fun provideUserGroupApiService(retrofit: Retrofit): UserGroupApiService =
        retrofit.create(UserGroupApiService::class.java)

    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    fun provideUsernameService(retrofit: Retrofit): UsernameApiService =
        retrofit.create(UsernameApiService::class.java)

}