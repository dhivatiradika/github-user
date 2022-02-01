package com.dhiva.githubuser.core.domain.repository

import com.dhiva.githubuser.core.data.Resource
import com.dhiva.githubuser.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    fun searchUser(query: String): Flow<com.dhiva.githubuser.core.data.Resource<List<User>>>

    fun getUsers(): Flow<com.dhiva.githubuser.core.data.Resource<List<User>>>

    fun getFavoriteUser(): Flow<List<User>>

    fun setFavoriteUser(user: User, state: Boolean)

    fun getUser(username: String): Flow<com.dhiva.githubuser.core.data.Resource<User>>

    fun getFollowers(username: String, page: Int): Flow<com.dhiva.githubuser.core.data.Resource<List<User>>>

    fun getFollowing(username: String, page: Int): Flow<com.dhiva.githubuser.core.data.Resource<List<User>>>
}