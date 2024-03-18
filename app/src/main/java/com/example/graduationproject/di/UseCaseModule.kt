package com.example.graduationproject.di


import com.example.graduationproject.domain.repository.GroupRepository
import com.example.graduationproject.domain.repository.SessionRepository
import com.example.graduationproject.domain.repository.UserGroupRepository
import com.example.graduationproject.domain.repository.UserRepository
import com.example.graduationproject.domain.usecase.FetchUserGroupsUseCase
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
    fun provideSignUpUseCase(repository: SessionRepository, userRepository: UserRepository): SignUpUseCase {
        return SignUpUseCase(repository, userRepository)
    }

    @Provides
    fun provideFetchUserGroupsUseCase(
        groupRepository: GroupRepository,
        userGroupRepository: UserGroupRepository
    ): FetchUserGroupsUseCase {
        return FetchUserGroupsUseCase(groupRepository, userGroupRepository)
    }
}
