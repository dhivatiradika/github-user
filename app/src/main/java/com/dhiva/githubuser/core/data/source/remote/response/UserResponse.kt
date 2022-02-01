package com.dhiva.githubuser.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("followers")
	val followers: Int?,

	@field:SerializedName("name")
	val name: String?,

	@field:SerializedName("avatar_url")
	val avatarUrl: String?,

	@field:SerializedName("following")
	val following: Int?,

	@field:SerializedName("company")
	val company: String?,

	@field:SerializedName("location")
	val location: String?,

	@field:SerializedName("public_repos")
	val publicRepos: Int?,

	@field:SerializedName("login")
	val login: String?
) : Parcelable
