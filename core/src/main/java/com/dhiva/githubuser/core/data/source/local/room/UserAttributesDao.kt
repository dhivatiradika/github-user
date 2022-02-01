package com.dhiva.githubuser.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dhiva.githubuser.core.data.source.local.entity.UserAttributesEntity

@Dao
interface UserAttributesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userAttributes: List<UserAttributesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(userAttributesEntity: UserAttributesEntity)

    @Query("DELETE FROM user_attributes WHERE user_id = :userId AND type = 3")
    fun deleteFavorite(userId: Int)
}