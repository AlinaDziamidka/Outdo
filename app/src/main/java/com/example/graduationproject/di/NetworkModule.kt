package com.example.graduationproject.di


import com.example.graduationproject.data.remote.api.NetworkClientConfig
import com.example.graduationproject.data.remote.api.service.AchievementApiService
import com.example.graduationproject.data.remote.api.service.AuthApiService
import com.example.graduationproject.data.remote.api.service.CategoryApiService
import com.example.graduationproject.data.remote.api.service.ChallengeAchievementApiService
import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.api.service.DeviceRegistrationApiService
import com.example.graduationproject.data.remote.api.service.GroupApiService
import com.example.graduationproject.data.remote.api.service.GroupChallengeApiService
import com.example.graduationproject.data.remote.api.service.PushNotificationApiService
import com.example.graduationproject.data.remote.api.service.UploadPhotoApiService
import com.example.graduationproject.data.remote.api.service.UserAchievementApiService
import com.example.graduationproject.data.remote.api.service.UserGroupApiService
import com.example.graduationproject.data.remote.api.service.UserApiService
import com.example.graduationproject.data.remote.api.service.UserFriendApiService
import com.example.graduationproject.data.remote.api.service.UserNotificationsApiService
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
    fun provideCategoryApiService(retrofit: Retrofit): CategoryApiService =
        retrofit.create(CategoryApiService::class.java)

    @Provides
    fun provideChallengeAchievementApiService(retrofit: Retrofit): ChallengeAchievementApiService =
        retrofit.create(ChallengeAchievementApiService::class.java)

    @Provides
    fun provideChallengeApiService(retrofit: Retrofit): ChallengeApiService =
        retrofit.create(ChallengeApiService::class.java)

    @Provides
    fun provideGroupApiService(retrofit: Retrofit): GroupApiService =
        retrofit.create(GroupApiService::class.java)

    @Provides
    fun provideGroupChallengeApiService(retrofit: Retrofit): GroupChallengeApiService =
        retrofit.create(GroupChallengeApiService::class.java)

    @Provides
    fun provideUserGroupApiService(retrofit: Retrofit): UserGroupApiService =
        retrofit.create(UserGroupApiService::class.java)

    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    fun provideUsernameService(retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    fun provideUserFriendService(retrofit: Retrofit): UserFriendApiService =
        retrofit.create(UserFriendApiService::class.java)

    @Provides
    fun provideUserAchievementService(retrofit: Retrofit): UserAchievementApiService =
        retrofit.create(UserAchievementApiService::class.java)

    @Provides
    fun provideDeviceRegistrationApiService(retrofit: Retrofit): DeviceRegistrationApiService =
        retrofit.create(DeviceRegistrationApiService::class.java)

    @Provides
    fun providePushNotificationApiService(retrofit: Retrofit): PushNotificationApiService =
        retrofit.create(PushNotificationApiService::class.java)

    @Provides
    fun provideUserNotificationApiService(retrofit: Retrofit): UserNotificationsApiService =
        retrofit.create(UserNotificationsApiService::class.java)

    @Provides
    fun provideUploadPhotoApiService(retrofit: Retrofit): UploadPhotoApiService =
        retrofit.create(UploadPhotoApiService::class.java)
}