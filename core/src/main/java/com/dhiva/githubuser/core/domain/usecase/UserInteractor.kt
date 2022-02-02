package com.dhiva.githubuser.core.domain.usecase

import com.dhiva.githubuser.core.data.Resource
import com.dhiva.githubuser.core.domain.model.User
import com.dhiva.githubuser.core.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow

class UserInteractor(private val userRepository: IUserRepository): UserUseCase {
    override fun searchUser(query: String): Flow<Resource<List<User>>> = userRepository.searchUser(query)

    override fun getUsers(): Flow<Resource<List<User>>> = userRepository.getUsers()

    override fun getFavoriteUser(): Flow<List<User>> = userRepository.getFavoriteUser()

    override fun setFavoriteUser(user: User, state: Boolean) = userRepository.setFavoriteUser(user, state)

    override fun getUser(username: String): Flow<Resource<User>> = userRepository.getUser(username)

    override fun getFollowers(username: String, page: Int): Flow<Resource<List<User>>> = userRepository.getFollowers(username, page)

    override fun getFollowing(username: String, page: Int): Flow<Resource<List<User>>> = userRepository.getFollowing(username, page)

}