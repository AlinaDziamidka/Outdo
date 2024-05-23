package com.example.graduationproject.di

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.repository.remote.SessionRepository
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import com.example.graduationproject.domain.usecase.SignInUseCase
import com.example.graduationproject.domain.usecase.SignUpUseCase
import com.example.graduationproject.domain.usecase.remote.FetchRemoteUserGroupChallengesUseCase
import com.example.graduationproject.domain.usecase.remote.FetchRemoteUserGroupsUseCase
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
    ): FetchRemoteUserGroupChallengesUseCase {
        return FetchRemoteUserGroupChallengesUseCase(remoteLoadManager, localLoadManager)
    }

    @Provides
    fun provideFetchRemoteUserGroupsUseCase(
        @Remote remoteLoadManager: LoadManager,
        @Local localLoadManager: LoadManager
    ) : FetchRemoteUserGroupsUseCase{
        return FetchRemoteUserGroupsUseCase(remoteLoadManager, localLoadManager)
    }

}
