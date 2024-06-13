package com.example.graduationproject.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
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
import com.example.graduationproject.data.local.database.model.AchievementModel
import com.example.graduationproject.data.local.database.model.CategoryModel
import com.example.graduationproject.data.local.database.model.ChallengeModel
import com.example.graduationproject.data.local.database.model.ChallengeAchievementModel
import com.example.graduationproject.data.local.database.model.GroupModel
import com.example.graduationproject.data.local.database.model.GroupChallengeModel
import com.example.graduationproject.data.local.database.model.UserAchievementModel
import com.example.graduationproject.data.local.database.model.UserFriendModel
import com.example.graduationproject.data.local.database.model.UserModel
import com.example.graduationproject.data.local.database.model.UserGroupModel

@Database(
    entities = [UserModel::class, GroupModel::class, ChallengeModel::class, AchievementModel::class,
        CategoryModel::class, GroupChallengeModel::class, UserGroupModel::class, ChallengeAchievementModel::class,
        UserFriendModel::class, UserAchievementModel::class], version = 2
)

abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun achievementDao(): AchievementDao
    abstract fun categoryDao(): CategoryDao
    abstract fun groupChallengeDao(): GroupChallengeDao
    abstract fun userGroupDao(): UserGroupDao
    abstract fun challengeAchievementDao(): ChallengeAchievementDao
    abstract fun userFriendDao(): UserFriendDao

    abstract fun userAchievementDao(): UserAchievementDao
}