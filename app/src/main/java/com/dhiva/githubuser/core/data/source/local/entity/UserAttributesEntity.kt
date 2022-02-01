package com.dhiva.githubuser.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_attributes")
data class UserAttributesEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "owner")
    var owner: String,

    @ColumnInfo(name = "type")
    var type: Int,

    @ColumnInfo(name = "user_id")
    var userId: Int,
)