package com.dhiva.githubuser.core.data.source.local.room

import androidx.room.*
import com.dhiva.githubuser.core.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users: List<UserEntity>)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getAllUser(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE login LIKE '%' || :query || '%' ")
    fun searchUser(query: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE login = :username")
    fun getUserByUsername(username: String): Flow<UserEntity>

    @Query("SELECT * FROM  user WHERE id IN (SELECT user_id FROM user_attributes WHERE type = 3)")
    fun getFavoriteUser(): Flow<List<UserEntity>>

    @Update
    fun updateFavorite(user: UserEntity)

    @Query("SELECT * FROM  user WHERE id IN (SELECT user_id FROM user_attributes WHERE owner = :username AND type = 1)")
    fun getAllFollowers(username: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM  user WHERE id IN (SELECT user_id FROM user_attributes WHERE owner = :username AND type = 2)")
    fun getAllFollowing(username: String): Flow<List<UserEntity>>
}