package com.example.graduationproject.di

import com.example.graduationproject.data.local.database.dao.AchievementDao
import com.example.graduationproject.data.local.database.dao.ChallengeAchievementDao
import com.example.graduationproject.data.local.database.dao.ChallengeDao
import com.example.graduationproject.data.local.database.dao.GroupChallengeDao
import com.example.graduationproject.data.local.database.dao.GroupDao
import com.example.graduationproject.data.local.database.dao.UserDao
import com.example.graduationproject.data.local.database.dao.UserGroupDao
import com.example.graduationproject.data.local.repository.AchievementLocalRepositoryImpl
import com.example.graduationproject.data.local.repository.ChallengeAchievementsLocalRepositoryImpl
import com.example.graduationproject.data.local.repository.ChallengeLocalRepositoryImpl
import com.example.graduationproject.data.local.repository.GroupChallengeLocalRepositoryImpl
import com.example.graduationproject.data.local.repository.GroupLocalRepositoryImpl
import com.example.graduationproject.data.local.repository.UserGroupLocalRepositoryImpl
import com.example.graduationproject.data.local.repository.UserLocalRepositoryImpl
import com.example.graduationproject.data.remote.api.service.AchievementApiService
import com.example.graduationproject.data.remote.api.service.ChallengeAchievementApiService
import com.example.graduationproject.data.remote.api.service.ChallengeApiService
import com.example.graduationproject.data.remote.api.service.GroupApiService
import com.example.graduationproject.data.remote.api.service.GroupChallengeApiService
import com.example.graduationproject.data.remote.api.service.UserApiService
import com.example.graduationproject.data.remote.api.service.UserGroupApiService
import com.example.graduationproject.data.remote.repository.AchievementRemoteRepositoryImpl
import com.example.graduationproject.data.remote.repository.ChallengeAchievementRemoteRepositoryImpl
import com.example.graduationproject.data.remote.repository.ChallengeRemoteRepositoryImpl
import com.example.graduationproject.data.remote.repository.GroupChallengeRemoteRepositoryImpl
import com.example.graduationproject.data.remote.repository.GroupRemoteRepositoryImpl
import com.example.graduationproject.data.remote.repository.SessionRepositoryImpl
import com.example.graduationproject.data.remote.repository.UserGroupRemoteRepositoryImpl
import com.example.graduationproject.data.remote.repository.UserRemoteRepositoryImpl
import com.example.graduationproject.domain.repository.local.AchievementLocalRepository
import com.example.graduationproject.domain.repository.local.ChallengeAchievementsLocalRepository
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.repository.remote.AchievementRemoteRepository
import com.example.graduationproject.domain.repository.remote.ChallengeAchievementRemoteRepository
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
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGroupRemoteRepository(groupApiService: GroupApiService): GroupRemoteRepository {
        return GroupRemoteRepositoryImpl(groupApiService)
    }

    @Provides
    @Singleton
    fun provideUserGroupRemoteRepository(userGroupApiService: UserGroupApiService): UserGroupRemoteRepository {
        return UserGroupRemoteRepositoryImpl(userGroupApiService)
    }

    @Provides
    @Singleton
    fun provideUserRemoteRepository(userApiService: UserApiService): UserRemoteRepository {
        return UserRemoteRepositoryImpl(userApiService)
    }

    @Provides
    @Singleton
    fun provideGroupChallengeRemoteRepository(
        groupChallengeApiService: GroupChallengeApiService,
        challengeApiService: ChallengeApiService
    ): GroupChallengeRemoteRepository {
        return GroupChallengeRemoteRepositoryImpl(groupChallengeApiService, challengeApiService)
    }

    @Provides
    @Singleton
    fun provideChallengeRemoteRepository(challengeApiService: ChallengeApiService): ChallengeRemoteRepository {
        return ChallengeRemoteRepositoryImpl(challengeApiService)
    }

    @Provides
    @Singleton
    fun provideAchievementRemoteRepository(achievementApiService: AchievementApiService): AchievementRemoteRepository {
        return AchievementRemoteRepositoryImpl(achievementApiService)
    }

    @Provides
    @Singleton
    fun provideChallengeAchievementsRemoteRepository(
        challengeAchievementApiService: ChallengeAchievementApiService,
        achievementApiService: AchievementApiService
    ): ChallengeAchievementRemoteRepository {
        return ChallengeAchievementRemoteRepositoryImpl(
            challengeAchievementApiService,
            achievementApiService
        )
    }

    @Provides
    @Singleton
    fun provideUserLocalRepository(userDao: UserDao): UserLocalRepository {
        return UserLocalRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun provideUserGroupLocalRepository(userGroupDao: UserGroupDao): UserGroupLocalRepository {
        return UserGroupLocalRepositoryImpl(userGroupDao)
    }

    @Provides
    @Singleton
    fun provideGroupLocalRepository(groupDao: GroupDao): GroupLocalRepository {
        return GroupLocalRepositoryImpl(groupDao)
    }

    @Provides
    @Singleton
    fun provideGroupChallengeLocalRepository(groupChallengeDao: GroupChallengeDao): GroupChallengeLocalRepository {
        return GroupChallengeLocalRepositoryImpl(groupChallengeDao)
    }

    @Provides
    @Singleton
    fun provideChallengeLocalRepository(challengeDao: ChallengeDao): ChallengeLocalRepository {
        return ChallengeLocalRepositoryImpl(challengeDao)
    }

    @Provides
    @Singleton
    fun provideAchievementLocalRepository(achievementDao: AchievementDao): AchievementLocalRepository {
        return AchievementLocalRepositoryImpl(achievementDao)
    }

    @Provides
    @Singleton
    fun provideChallengeAchievementsLocalRepository(challengeAchievementDao: ChallengeAchievementDao): ChallengeAchievementsLocalRepository {
        return ChallengeAchievementsLocalRepositoryImpl(challengeAchievementDao)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryBindModule {

    @Binds
    fun bindSessionRepository(sessionRepositoryImpl: SessionRepositoryImpl): SessionRepository

}
