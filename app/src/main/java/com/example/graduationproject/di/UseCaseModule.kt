package com.example.graduationproject.di

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.remote.SessionRepository
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import com.example.graduationproject.domain.usecase.FetchChallengeAchievementUseCase
import com.example.graduationproject.domain.usecase.FetchChallengeNameUseCase
import com.example.graduationproject.domain.usecase.SignInUseCase
import com.example.graduationproject.domain.usecase.SignUpUseCase
import com.example.graduationproject.domain.usecase.FetchUserGroupChallengesUseCase
import com.example.graduationproject.domain.usecase.FetchUserGroupsUseCase
import com.example.graduationproject.domain.usecase.FetchDailyAchievementUseCase
import com.example.graduationproject.domain.usecase.FetchGroupChallengesUseCase
import com.example.graduationproject.domain.usecase.FetchGroupNameUseCase
import com.example.graduationproject.domain.usecase.FetchGroupParticipantsUseCase
import com.example.graduationproject.domain.usecase.FetchWeekChallengeUseCase
import com.example.graduationproject.domain.util.LoadManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideSignInUseCase(repository: SessionRepository): SignInUseCase {
        return SignInUseCase(repository)
    }

    @Provides
    fun provideSignUpUseCase(
        repository: SessionRepository,
        userRemoteRepository: UserRemoteRepository
    ): SignUpUseCase {
        return SignUpUseCase(repository, userRemoteRepository)
    }

    @Provides
    fun provideFetchUserGroupChallengeUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchUserGroupChallengesUseCase {
        return FetchUserGroupChallengesUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    fun provideFetchRemoteUserGroupsUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchUserGroupsUseCase {
        return FetchUserGroupsUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    fun provideFetchDailyAchievementUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchDailyAchievementUseCase {
        return FetchDailyAchievementUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    fun provideFetchWeekChallengeUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchWeekChallengeUseCase {
        return FetchWeekChallengeUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    fun provideFetchGroupChallengesUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchGroupChallengesUseCase {
        return FetchGroupChallengesUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    fun provideFetchGroupNameUseCase(repository: GroupLocalRepository): FetchGroupNameUseCase {
        return FetchGroupNameUseCase(repository)
    }

    @Provides
    fun provideFetchGroupParticipantsUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchGroupParticipantsUseCase {
        return FetchGroupParticipantsUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    fun provideFetchChallengeNameUseCase(repository: ChallengeLocalRepository): FetchChallengeNameUseCase {
        return FetchChallengeNameUseCase(repository)
    }

    @Provides
    fun provideFetchChallengeAchievementUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ): FetchChallengeAchievementUseCase {
        return FetchChallengeAchievementUseCase(remoteLoadManager, localLoadManager)
    }



}
