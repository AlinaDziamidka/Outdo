package com.example.graduationproject.di

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.repository.local.AchievementLocalRepository
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserAchievementLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.repository.local.UserNotificationsLocalRepository
import com.example.graduationproject.domain.repository.remote.DeviceRegistrationRepository
import com.example.graduationproject.domain.repository.remote.GroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.PhotoUploadRemoteRepository
import com.example.graduationproject.domain.repository.remote.PushNotificationRepository
import com.example.graduationproject.domain.repository.remote.SessionRepository
import com.example.graduationproject.domain.repository.remote.UserAchievementRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserNotificationsRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import com.example.graduationproject.domain.usecase.CreateGroupUseCase
import com.example.graduationproject.domain.usecase.DeleteNotificationUseCase
import com.example.graduationproject.domain.usecase.DeleteUserGroupUseCase
import com.example.graduationproject.domain.usecase.DeviceRegistrationUseCase
import com.example.graduationproject.domain.usecase.FetchAchievementDescriptionUseCase
import com.example.graduationproject.domain.usecase.FetchChallengeAchievementUseCase
import com.example.graduationproject.domain.usecase.FetchChallengeDescriptionUseCase
import com.example.graduationproject.domain.usecase.FetchCompletedFriendsUseCase
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
import com.example.graduationproject.domain.usecase.NotificationUseCase
import com.example.graduationproject.domain.usecase.PhotoUploadUseCase
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

    @Provides
    @ViewModelScoped
    fun provideDeviceRegistrationUseCase(
        deviceRegistrationRepository: DeviceRegistrationRepository
    ): DeviceRegistrationUseCase {
        return DeviceRegistrationUseCase(
            deviceRegistrationRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideNotificationUseCase(
        deviceRegistrationRepository: DeviceRegistrationRepository,
        pushNotificationRepository: PushNotificationRepository,
        userNotificationsRemoteRepository: UserNotificationsRemoteRepository
    ): NotificationUseCase {
        return NotificationUseCase(
            deviceRegistrationRepository,
            pushNotificationRepository,
            userNotificationsRemoteRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteUserGroupUseCase(
        userGroupRemoteRepository: UserGroupRemoteRepository,
        userGroupLocalRepository: UserGroupLocalRepository
    ): DeleteUserGroupUseCase {
        return DeleteUserGroupUseCase(
            userGroupRemoteRepository,
            userGroupLocalRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteNotificationUseCase(
        userNotificationsRemoteRepository: UserNotificationsRemoteRepository,
        userNotificationsLocalRepository: UserNotificationsLocalRepository
    ): DeleteNotificationUseCase {
        return DeleteNotificationUseCase(
            userNotificationsRemoteRepository,
            userNotificationsLocalRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideFetchAchievementDescriptionUseCase(
        achievementLocalRepository: AchievementLocalRepository
    ): FetchAchievementDescriptionUseCase {
        return FetchAchievementDescriptionUseCase(achievementLocalRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchCompletedFriendsUseCase(
        userAchievementRemoteRepository: UserAchievementRemoteRepository,
        userAchievementLocalRepository: UserAchievementLocalRepository,
        userRemoteRepository: UserRemoteRepository,
        userLocalRepository: UserLocalRepository
    ): FetchCompletedFriendsUseCase {
        return FetchCompletedFriendsUseCase(
            userAchievementRemoteRepository,
            userAchievementLocalRepository,
            userRemoteRepository,
            userLocalRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun providePhotoUploadUseCase(
        photoUploadRemoteRepository: PhotoUploadRemoteRepository,
        userAchievementRemoteRepository: UserAchievementRemoteRepository,
        userAchievementLocalRepository: UserAchievementLocalRepository
    ): PhotoUploadUseCase {
        return PhotoUploadUseCase(
            photoUploadRemoteRepository, userAchievementRemoteRepository, userAchievementLocalRepository
        )
    }


}
