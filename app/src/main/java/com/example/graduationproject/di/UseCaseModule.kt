package com.example.graduationproject.di

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.repository.remote.GroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.SessionRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import com.example.graduationproject.domain.usecase.CreateGroupUseCase
import com.example.graduationproject.domain.usecase.FetchChallengeAchievementUseCase
import com.example.graduationproject.domain.usecase.FetchChallengeDescriptionUseCase
import com.example.graduationproject.domain.usecase.SignInUseCase
import com.example.graduationproject.domain.usecase.SignUpUseCase
import com.example.graduationproject.domain.usecase.FetchUserGroupChallengesUseCase
import com.example.graduationproject.domain.usecase.FetchUserGroupsUseCase
import com.example.graduationproject.domain.usecase.FetchDailyAchievementUseCase
import com.example.graduationproject.domain.usecase.FetchFriendsUseCase
import com.example.graduationproject.domain.usecase.FetchGroupChallengesUseCase
import com.example.graduationproject.domain.usecase.FetchGroupNameUseCase
import com.example.graduationproject.domain.usecase.FetchGroupParticipantsUseCase
import com.example.graduationproject.domain.usecase.FetchWeekChallengeUseCase
import com.example.graduationproject.domain.usecase.SetGroupParticipantsUseCase
import com.example.graduationproject.domain.util.LoadManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideSignInUseCase(repository: SessionRepository): SignInUseCase {
        return SignInUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideSignUpUseCase(
        repository: SessionRepository,
        userRemoteRepository: UserRemoteRepository
    ): SignUpUseCase {
        return SignUpUseCase(repository, userRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchUserGroupChallengeUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchUserGroupChallengesUseCase {
        return FetchUserGroupChallengesUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchRemoteUserGroupsUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchUserGroupsUseCase {
        return FetchUserGroupsUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchDailyAchievementUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchDailyAchievementUseCase {
        return FetchDailyAchievementUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchWeekChallengeUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchWeekChallengeUseCase {
        return FetchWeekChallengeUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchGroupChallengesUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchGroupChallengesUseCase {
        return FetchGroupChallengesUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchGroupNameUseCase(repository: GroupLocalRepository): FetchGroupNameUseCase {
        return FetchGroupNameUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchGroupParticipantsUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchGroupParticipantsUseCase {
        return FetchGroupParticipantsUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchChallengeDescriptionUseCase(
        challengeLocalRepository: ChallengeLocalRepository,
        userLocalRepository: UserLocalRepository
    ): FetchChallengeDescriptionUseCase {
        return FetchChallengeDescriptionUseCase(challengeLocalRepository, userLocalRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchChallengeAchievementUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchChallengeAchievementUseCase {
        return FetchChallengeAchievementUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    @ViewModelScoped
    fun provideCreateGroupUseCaseUseCase(
        groupRemoteRepository: GroupRemoteRepository,
        groupLocalRepository: GroupLocalRepository,
        userGroupRemoteRepository: UserGroupRemoteRepository,
        userGroupLocalRepository: UserGroupLocalRepository
    ): CreateGroupUseCase {
        return CreateGroupUseCase(
            groupRemoteRepository,
            groupLocalRepository,
            userGroupRemoteRepository,
            userGroupLocalRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideFetchFriendsUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchFriendsUseCase {
        return FetchFriendsUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    @ViewModelScoped
    fun provideSetGroupParticipantsUseCase(
        userGroupRemoteRepository: UserGroupRemoteRepository,
        userGroupLocalRepository: UserGroupLocalRepository
    ): SetGroupParticipantsUseCase {
        return SetGroupParticipantsUseCase(
            userGroupRemoteRepository,
            userGroupLocalRepository
        )
    }

}
