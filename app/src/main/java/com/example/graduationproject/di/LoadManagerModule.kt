package com.example.graduationproject.di

import com.example.graduationproject.data.local.LocalLoadManager
import com.example.graduationproject.data.remote.RemoteLoadManager
import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.util.LoadManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LoadManagerModule {

    @Provides
    @Local
    fun provideLocalLoadManager(localLoadManager: LocalLoadManager): LoadManager{
        return localLoadManager
    }

    @Provides
    @Remote
    fun provideRemoteLoadManager(remoteLoadManager: RemoteLoadManager): LoadManager {
        return remoteLoadManager
    }
}