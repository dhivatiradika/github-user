package com.dhiva.githubuser.data.remote

import com.dhiva.githubuser.BuildConfig
import com.dhiva.githubuser.data.remote.response.SearchUserResponse
import com.dhiva.githubuser.data.remote.response.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun searchUser(
        @Query("q") query: String
    ) : Call<SearchUserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getUser(
        @Path("username") username: String
    ) : Call<User>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowers(
        @Path("username") username: String,
        @Query("page") page: Int
    ) : Call<List<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowing(
        @Path("username") username: String,
        @Query("page") page: Int
    ) : Call<List<User>>

}