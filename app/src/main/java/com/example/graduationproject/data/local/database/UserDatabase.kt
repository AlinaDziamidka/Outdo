package com.example.graduationproject.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.graduationproject.App
import com.example.graduationproject.data.local.database.dao.AchievementDao
import com.example.graduationproject.data.local.database.dao.AwardDao
import com.example.graduationproject.data.local.database.dao.CategoryDao
import com.example.graduationproject.data.local.database.dao.ChallengeAchievementDao
import com.example.graduationproject.data.local.database.dao.ChallengeDao
import com.example.graduationproject.data.local.database.dao.CompetitionDao
import com.example.graduationproject.data.local.database.dao.GroupChallengeDao
import com.example.graduationproject.data.local.database.dao.GroupDao
import com.example.graduationproject.data.local.database.dao.UserAwardDao
import com.example.graduationproject.data.local.database.dao.UserDao
import com.example.graduationproject.data.local.database.dao.UserGroupDao
import com.example.graduationproject.data.local.database.model.Achievement
import com.example.graduationproject.data.local.database.model.Award
import com.example.graduationproject.data.local.database.model.Category
import com.example.graduationproject.data.local.database.model.Challenge
import com.example.graduationproject.data.local.database.model.ChallengeAchievement
import com.example.graduationproject.data.local.database.model.Competition
import com.example.graduationproject.data.local.database.model.Group
import com.example.graduationproject.data.local.database.model.GroupChallenge
import com.example.graduationproject.data.local.database.model.User
import com.example.graduationproject.data.local.database.model.UserAward
import com.example.graduationproject.data.local.database.model.UserCompetition
import com.example.graduationproject.data.local.database.model.UserGroup

@Database(
    entities = [User::class, Group::class, Challenge::class,
        Competition::class, Award::class, Achievement::class,
        Category::class, GroupChallenge::class, UserGroup::class,
        UserCompetition::class, ChallengeAchievement::class, UserAward::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun competitionDao(): CompetitionDao
    abstract fun awardDao(): AwardDao
    abstract fun achievementDao(): AchievementDao
    abstract fun categoryDao(): CategoryDao
    abstract fun groupChallengeDao(): GroupChallengeDao
    abstract fun userGroupDao(): UserGroupDao
    abstract fun userCompetitionDao(): UserCompetition
    abstract fun challengeAchievementDao(): ChallengeAchievementDao
    abstract fun userAwardDao(): UserAwardDao

    companion object {

        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    App.instance,
                    UserDatabase::class.java, "userdb"
                )
//                    .addMigrations(MIGRATION_1_2)
//                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}