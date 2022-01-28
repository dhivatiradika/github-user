package com.dhiva.githubuser.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SearchUserResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<User>
)

@Parcelize
data class User(

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
