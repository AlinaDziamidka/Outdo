package com.example.graduationproject.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.graduationproject.App
import com.example.graduationproject.data.local.database.dao.GroupDao
import com.example.graduationproject.data.local.database.dao.UserDao
import com.example.graduationproject.data.local.database.model.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao

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