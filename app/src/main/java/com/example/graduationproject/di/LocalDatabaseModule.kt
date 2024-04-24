package com.example.graduationproject.di

import android.content.Context
import androidx.room.Room
import com.example.graduationproject.App
import com.example.graduationproject.data.local.database.UserDatabase
import com.example.graduationproject.data.local.database.dao.AwardDao
import com.example.graduationproject.data.local.database.dao.CategoryDao
import com.example.graduationproject.data.local.database.dao.ChallengeAchievementDao
import com.example.graduationproject.data.local.database.dao.ChallengeDao
import com.example.graduationproject.data.local.database.dao.CompetitionDao
import com.example.graduationproject.data.local.database.dao.GroupChallengeDao
import com.example.graduationproject.data.local.database.dao.GroupDao
import com.example.graduationproject.data.local.database.dao.UserAwardDao
import com.example.graduationproject.data.local.database.dao.UserCompetitionDao
import com.example.graduationproject.data.local.database.dao.UserDao
import com.example.graduationproject.data.local.database.dao.UserGroupDao
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
    fun provideAwardDao(database: UserDatabase): AwardDao {
        return database.awardDao()
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
    fun provideCompetitionDao(database: UserDatabase): CompetitionDao {
        return database.competitionDao()
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
    fun provideUserAwardDao(database: UserDatabase): UserAwardDao {
        return database.userAwardDao()
    }

    @Provides
    fun provideUserCompetitionDao(database: UserDatabase): UserCompetitionDao {
        return database.userCompetitionDao()
    }

    @Provides
    fun provideUserGroupDao(database: UserDatabase): UserGroupDao {
        return database.userGroupDao()
    }

}
