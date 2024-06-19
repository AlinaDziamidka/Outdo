package com.example.graduationproject.di

import android.app.Application
import com.backendless.Backendless
import com.backendless.Messaging
import com.example.graduationproject.data.remote.api.NetworkClientConfig
//import com.example.graduationproject.data.remote.api.PushNotificationRegistrar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackendlessModule {

//    @Provides
//    @Singleton
//    fun provideBackendlessMessaging(): Messaging {
//        return Backendless.Messaging
//    }

//    @Provides
//    @Singleton
//    fun providePushNotificationRegistrar(messaging: Messaging): PushNotificationRegistrar {
//        return PushNotificationRegistrar(messaging)
//    }
}