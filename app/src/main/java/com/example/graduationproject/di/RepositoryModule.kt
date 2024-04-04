package com.example.graduationproject.di

import com.example.graduationproject.data.local.database.dao.UserDao
import com.example.graduationproject.data.local.repository.UserLocalRepositoryImpl
import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.api.service.GroupApiService
import com.example.graduationproject.data.remote.api.service.GroupChallengeApiService
import com.example.graduationproject.data.remote.api.service.UserApiService
import com.example.graduationproject.data.remote.api.service.UserGroupApiService
import com.example.graduationproject.data.remote.repository.ChallengeRepositoryImpl
import com.example.graduationproject.data.remote.repository.GroupChallengeRepositoryImpl
import com.example.graduationproject.data.remote.repository.GroupRepositoryImpl
import com.example.graduationproject.data.remote.repository.SessionRepositoryImpl
import com.example.graduationproject.data.remote.repository.UserGroupRepositoryImpl
import com.example.graduationproject.data.remote.repository.UserRepositoryImpl
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.repository.remote.ChallengeRepository
import com.example.graduationproject.domain.repository.remote.GroupChallengeRepository
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
    fun provideUserRepository(userApiService: UserApiService): UserRepository {
        return UserRepositoryImpl(userApiService)
    }

    @Provides
    fun provideUserLocalRepository(userDao: UserDao): UserLocalRepository {
        return UserLocalRepositoryImpl(userDao)
    }

    @Provides
    fun provideGroupChallengeRepository(
        groupChallengeApiService: GroupChallengeApiService,
        challengeApiService: ChallengeApiService
    ): GroupChallengeRepository {
        return GroupChallengeRepositoryImpl(groupChallengeApiService, challengeApiService)
    }

    @Provides
    fun provideChallengeRepository(challengeApiService: ChallengeApiService): ChallengeRepository {
        return ChallengeRepositoryImpl(challengeApiService)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryBindModule {

    @Binds
    fun bindSessionRepository(sessionRepositoryImpl: SessionRepositoryImpl): SessionRepository

}
