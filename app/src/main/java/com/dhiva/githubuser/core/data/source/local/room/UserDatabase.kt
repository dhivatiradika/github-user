package com.dhiva.githubuser.core.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dhiva.githubuser.core.data.source.local.entity.UserAttributesEntity
import com.dhiva.githubuser.core.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class, UserAttributesEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userAttributesDao(): UserAttributesDao

    companion object{
        @Volatile
        private var INSTANCE: UserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserDatabase {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserDatabase::class.java, "user_database")
                        .build()
                }
            }
            return INSTANCE as UserDatabase
        }
    }

}