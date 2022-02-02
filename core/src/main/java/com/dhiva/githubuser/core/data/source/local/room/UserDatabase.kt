package com.dhiva.githubuser.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dhiva.githubuser.core.data.source.local.entity.UserAttributesEntity
import com.dhiva.githubuser.core.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class, UserAttributesEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userAttributesDao(): UserAttributesDao
}