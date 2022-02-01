package com.dhiva.githubuser.core.domain.repository

import com.dhiva.githubuser.core.data.Resource
import com.dhiva.githubuser.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    fun searchUser(query: String): Flow<Resource<List<User>>>

    fun getUsers(): Flow<Resource<List<User>>>

    fun getFavoriteUser(): Flow<List<User>>

    fun setFavoriteUser(user: User, state: Boolean)

    fun getUser(username: String): Flow<Resource<User>>

    fun getFollowers(username: String, page: Int): Flow<Resource<List<User>>>

    fun getFollowing(username: String, page: Int): Flow<Resource<List<User>>>
}