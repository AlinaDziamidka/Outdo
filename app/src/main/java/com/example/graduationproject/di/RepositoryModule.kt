package com.example.graduationproject.di

import com.example.graduationproject.data.remote.api.service.GroupApiService
import com.example.graduationproject.data.remote.api.service.UserGroupApiService
import com.example.graduationproject.data.repository.GroupRepositoryImpl
import com.example.graduationproject.data.repository.SessionRepositoryImpl
import com.example.graduationproject.data.repository.UserGroupRepositoryImpl
import com.example.graduationproject.data.repository.UsernameRepositoryImpl
import com.example.graduationproject.domain.repository.GroupRepository
import com.example.graduationproject.domain.repository.SessionRepository
import com.example.graduationproject.domain.repository.UserGroupRepository
import com.example.graduationproject.domain.repository.UsernameRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideGroupRepository(groupApiService: GroupApiService): GroupRepository {
        return GroupRepositoryImpl(groupApiService)
    }

    @Provides
    fun provideUserGroupRepository(userGroupApiService: UserGroupApiService): UserGroupRepository {
        return UserGroupRepositoryImpl(userGroupApiService)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryBindModule {

    @Binds
    fun bindSessionRepository(sessionRepositoryImpl: SessionRepositoryImpl): SessionRepository

    @Binds
    fun bindUsernameRepository(usernameRepositoryImpl: UsernameRepositoryImpl): UsernameRepository
}
