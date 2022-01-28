package com.dhiva.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dhiva.githubuser.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getAllUser(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user WHERE login = :username")
    fun getUserByUsername(username: String): LiveData<UserEntity>?
}