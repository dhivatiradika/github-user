package com.dhiva.githubuser.core.data.source.remote.network

import com.dhiva.githubuser.core.BuildConfig
import com.dhiva.githubuser.core.data.source.remote.response.ListUserResponse
import com.dhiva.githubuser.core.data.source.remote.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    suspend fun searchUser(
        @Query("q") query: String
    ) : ListUserResponse

    @GET("users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getUsers() : List<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getUser(
        @Path("username") username: String
    ) : UserResponse

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getFollowers(
        @Path("username") username: String,
        @Query("page") page: Int
    ) : List<UserResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getFollowing(
        @Path("username") username: String,
        @Query("page") page: Int
    ) : List<UserResponse>

}