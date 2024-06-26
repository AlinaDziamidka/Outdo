package com.example.graduationproject.di

import android.content.Context
import androidx.room.Room
import com.example.graduationproject.data.local.database.UserDatabase
import com.example.graduationproject.data.local.database.dao.AchievementDao
import com.example.graduationproject.data.local.database.dao.CategoryDao
import com.example.graduationproject.data.local.database.dao.ChallengeAchievementDao
import com.example.graduationproject.data.local.database.dao.ChallengeDao
import com.example.graduationproject.data.local.database.dao.GroupChallengeDao
import com.example.graduationproject.data.local.database.dao.GroupDao
import com.example.graduationproject.data.local.database.dao.UserAchievementDao
import com.example.graduationproject.data.local.database.dao.UserDao
import com.example.graduationproject.data.local.database.dao.UserFriendDao
import com.example.graduationproject.data.local.database.dao.UserGroupDao
import com.example.graduationproject.data.local.database.dao.UserNotificationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "userdb"
        ).build()
    }


    @Provides
    fun provideUserDao(database: UserDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideCategoryDao(database: UserDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideChallengeAchievementDao(database: UserDatabase): ChallengeAchievementDao {
        return database.challengeAchievementDao()
    }

    @Provides
    fun provideChallengeDao(database: UserDatabase): ChallengeDao {
        return database.challengeDao()
    }

    @Provides
    fun provideGroupChallengeDao(database: UserDatabase): GroupChallengeDao {
        return database.groupChallengeDao()
    }

    @Provides
    fun provideGroupDao(database: UserDatabase): GroupDao {
        return database.groupDao()
    }

    @Provides
    fun provideUserGroupDao(database: UserDatabase): UserGroupDao {
        return database.userGroupDao()
    }

    @Provides
    fun provideAchievementDao(database: UserDatabase): AchievementDao {
        return database.achievementDao()
    }

    @Provides
    fun provideUserAchievementDao(database: UserDatabase): UserAchievementDao {
        return database.userAchievementDao()
    }

    @Provides
    fun provideUserFriendDao(database: UserDatabase): UserFriendDao {
        return database.userFriendDao()
    }

    @Provides
    fun provideUserNotificationDao(database: UserDatabase): UserNotificationDao {
        return database.userNotificationDao()
    }
}
