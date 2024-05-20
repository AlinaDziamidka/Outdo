package com.example.graduationproject.di

import com.example.graduationproject.data.local.LocalLoadManager
import com.example.graduationproject.data.remote.RemoteLoadManager
import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.util.LoadManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LoadManagerModule {

    @Binds
    @Local
    abstract fun bindLocalLoadManager(localLoadManager: LocalLoadManager): LoadManager

    @Binds
    @Remote
    abstract fun bindRemoteLoadManager(remoteLoadManager: RemoteLoadManager): LoadManager

}