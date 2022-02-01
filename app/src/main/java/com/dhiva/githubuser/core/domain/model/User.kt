package com.dhiva.githubuser.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int,
    var login: String?,
    var avatarUrl: String?,
    var name: String?,
    var followers: Int?,
    var following: Int?,
    var company: String?,
    var location: String?,
    var publicRepos: Int?,
    var isFavorite: Boolean
) : Parcelable
