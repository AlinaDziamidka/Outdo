package com.example.graduationproject.di

import com.example.graduationproject.data.local.database.dao.AchievementDao
import com.example.graduationproject.data.local.database.dao.ChallengeDao
import com.example.graduationproject.data.local.database.dao.GroupChallengeDao
import com.example.graduationproject.data.local.database.dao.GroupDao
import com.example.graduationproject.data.local.database.dao.UserDao
import com.example.graduationproject.data.local.database.dao.UserGroupDao
import com.example.graduationproject.data.local.repository.AchievementLocalRepositoryImpl
import com.example.graduationproject.data.local.repository.ChallengeLocalRepositoryImpl
import com.example.graduationproject.data.local.repository.GroupChallengeLocalRepositoryImpl
import com.example.graduationproject.data.local.repository.GroupLocalRepositoryImpl
import com.example.graduationproject.data.local.repository.UserGroupLocalRepositoryImpl
import com.example.graduationproject.data.local.repository.UserLocalRepositoryImpl
import com.example.graduationproject.data.remote.api.service.AchievementApiService
import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.api.service.GroupApiService
import com.example.graduationproject.data.remote.api.service.GroupChallengeApiService
import com.example.graduationproject.data.remote.api.service.UserApiService
import com.example.graduationproject.data.remote.api.service.UserGroupApiService
import com.example.graduationproject.data.remote.repository.AchievementRemoteRepositoryImpl
import com.example.graduationproject.data.remote.repository.ChallengeRemoteRepositoryImpl
import com.example.graduationproject.data.remote.repository.GroupChallengeRemoteRepositoryImpl
import com.example.graduationproject.data.remote.repository.GroupRemoteRepositoryImpl
import com.example.graduationproject.data.remote.repository.SessionRepositoryImpl
import com.example.graduationproject.data.remote.repository.UserGroupRemoteRepositoryImpl
import com.example.graduationproject.data.remote.repository.UserRemoteRepositoryImpl
import com.example.graduationproject.domain.repository.local.AchievementLocalRepository
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.repository.remote.AchievementRemoteRepository
import com.example.graduationproject.domain.repository.remote.ChallengeRemoteRepository
import com.example.graduationproject.domain.repository.remote.GroupChallengeRemoteRepository
import com.example.graduationproject.domain.repository.remote.GroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.SessionRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideGroupRepository(groupApiService: GroupApiService): GroupRemoteRepository {
        return GroupRemoteRepositoryImpl(groupApiService)
    }

    @Provides
    fun provideUserGroupRepository(userGroupApiService: UserGroupApiService): UserGroupRemoteRepository {
        return UserGroupRemoteRepositoryImpl(userGroupApiService)
    }

    @Provides
    fun provideUserRepository(userApiService: UserApiService): UserRemoteRepository {
        return UserRemoteRepositoryImpl(userApiService)
    }

    @Provides
    fun provideUserLocalRepository(userDao: UserDao): UserLocalRepository {
        return UserLocalRepositoryImpl(userDao)
    }

    @Provides
    fun provideUserGroupLocalRepository(userGroupDao: UserGroupDao): UserGroupLocalRepository {
        return UserGroupLocalRepositoryImpl(userGroupDao)
    }

    @Provides
    fun provideGroupLocalRepository(groupDao: GroupDao): GroupLocalRepository {
        return GroupLocalRepositoryImpl(groupDao)
    }

    @Provides
    fun provideGroupChallengeLocalRepository(groupChallengeDao: GroupChallengeDao): GroupChallengeLocalRepository {
        return GroupChallengeLocalRepositoryImpl(groupChallengeDao)
    }

    @Provides
    fun provideChallengeLocalRepository(challengeDao: ChallengeDao): ChallengeLocalRepository {
        return ChallengeLocalRepositoryImpl(challengeDao)
    }

    @Provides
    fun provideGroupChallengeRepository(
        groupChallengeApiService: GroupChallengeApiService,
        challengeApiService: ChallengeApiService
    ): GroupChallengeRemoteRepository {
        return GroupChallengeRemoteRepositoryImpl(groupChallengeApiService, challengeApiService)
    }

    @Provides
    fun provideChallengeRepository(challengeApiService: ChallengeApiService): ChallengeRemoteRepository {
        return ChallengeRemoteRepositoryImpl(challengeApiService)
    }

    @Provides
    fun provideAchievementRepository(achievementApiService: AchievementApiService): AchievementRemoteRepository {
        return AchievementRemoteRepositoryImpl(achievementApiService)
    }

    @Provides
    fun provideAchievementLocalRepository(achievementDao: AchievementDao): AchievementLocalRepository {
        return AchievementLocalRepositoryImpl(achievementDao)
    }

}

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryBindModule {

    @Binds
    fun bindSessionRepository(sessionRepositoryImpl: SessionRepositoryImpl): SessionRepository

}
