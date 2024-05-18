package com.example.graduationproject.di

import com.example.graduationproject.domain.repository.remote.SessionRepository
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import com.example.graduationproject.domain.usecase.SignInUseCase
import com.example.graduationproject.domain.usecase.SignUpUseCase
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
    fun provideSignUpUseCase(repository: SessionRepository, userRemoteRepository: UserRemoteRepository): SignUpUseCase {
        return SignUpUseCase(repository, userRemoteRepository)
    }
}
