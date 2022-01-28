package com.dhiva.githubuser.core.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user")
@Parcelize
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "login")
    var login: String?,

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String?,

    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "followers")
    var followers: Int?,

    @ColumnInfo(name = "following")
    var following: Int?,

    @ColumnInfo(name = "company")
    var company: String?,

    @ColumnInfo(name = "location")
    var location: String?,

    @ColumnInfo(name = "public_repos")
    var publicRepos: Int?
) : Parcelable