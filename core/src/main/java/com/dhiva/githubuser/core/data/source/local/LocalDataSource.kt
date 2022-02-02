package com.dhiva.githubuser.core.data.source.local

import com.dhiva.githubuser.core.data.source.local.entity.UserAttributesEntity
import com.dhiva.githubuser.core.data.source.local.entity.UserEntity
import com.dhiva.githubuser.core.data.source.local.room.UserAttributesDao
import com.dhiva.githubuser.core.data.source.local.room.UserDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val userDao: UserDao, private val userAttributesDao: UserAttributesDao) {
    fun getAllUser(): Flow<List<UserEntity>> = userDao.getAllUser()

    fun getFavoriteUser(): Flow<List<UserEntity>> = userDao.getFavoriteUser()

    fun searchUser(query: String): Flow<List<UserEntity>> = userDao.searchUser(query)

    fun getUserByUsername(username: String): Flow<UserEntity> =
        userDao.getUserByUsername(username)

    suspend fun insert(users: List<UserEntity>) = userDao.insert(users)

    fun setFavorite(user: UserEntity, state: Boolean) {
        val data = UserAttributesEntity(
            owner = "",
            type = 3,
            userId = user.id
        )
        if (state){
            userAttributesDao.insertFavorite(data)
        } else{
            userAttributesDao.deleteFavorite(data.userId)
        }
    }

    suspend fun insertUserAtt(userAtt: List<UserAttributesEntity>) = userAttributesDao.insert(userAtt)

    fun getAllFollowers(username: String): Flow<List<UserEntity>> =
        userDao.getAllFollowers(username)

    fun getAllFollowing(username: String): Flow<List<UserEntity>> =
        userDao.getAllFollowing(username)
}