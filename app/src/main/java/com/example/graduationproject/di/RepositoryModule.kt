package com.example.graduationproject.di

import com.example.graduationproject.data.local.database.dao.UserDao
import com.example.graduationproject.data.local.repository.UserLocalRepositoryImpl
import com.example.graduationproject.data.remote.api.service.GroupApiService
import com.example.graduationproject.data.remote.api.service.UserApiService
import com.example.graduationproject.data.remote.api.service.UserGroupApiService
import com.example.graduationproject.data.remote.repository.GroupRepositoryImpl
import com.example.graduationproject.data.remote.repository.SessionRepositoryImpl
import com.example.graduationproject.data.remote.repository.UserGroupRepositoryImpl
import com.example.graduationproject.data.remote.repository.UserRepositoryImpl
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.repository.remote.GroupRepository
import com.example.graduationproject.domain.repository.remote.SessionRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRepository
import com.example.graduationproject.domain.repository.remote.UserRepository
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

    @Provides
    fun provideUserRepository(userApiService: UserApiService): UserRepository{
        return UserRepositoryImpl(userApiService)
    }

    @Provides
    fun provideUserLocalRepository(userDao: UserDao): UserLocalRepository{
        return UserLocalRepositoryImpl(userDao)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryBindModule {

    @Binds
    fun bindSessionRepository(sessionRepositoryImpl: SessionRepositoryImpl): SessionRepository

}
