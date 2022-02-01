package com.dhiva.githubuser.core.utils

import com.dhiva.githubuser.core.data.source.local.entity.UserEntity
import com.dhiva.githubuser.core.data.source.remote.response.UserResponse
import com.dhiva.githubuser.core.domain.model.User

object DataMapper {
    fun mapResponsesToEntities(input: List<UserResponse>): List<UserEntity> {
        val userList = ArrayList<UserEntity>()
        input.map {
            val user = UserEntity(
                id = it.id,
                login = it.login,
                avatarUrl = it.avatarUrl,
                name = it.name,
                followers = it.followers,
                following = it.following,
                company = it.company,
                location = it.location,
                publicRepos = it.publicRepos,
                isFavorite = false
            )
            userList.add(user)
        }
        return userList
    }

    fun mapEntitiesToDomain(input: List<UserEntity>): List<User> =
        input.map {
            User(
                id = it.id,
                login = it.login,
                avatarUrl = it.avatarUrl,
                name = it.name,
                followers = it.followers,
                following = it.following,
                company = it.company,
                location = it.location,
                publicRepos = it.publicRepos,
                isFavorite = it.isFavorite
            )
        }

    fun mapSingleEntitiesToDomain(input: UserEntity): User =
        User(
            id = input.id,
            login = input.login,
            avatarUrl = input.avatarUrl,
            name = input.name,
            followers = input.followers,
            following = input.following,
            company = input.company,
            location = input.location,
            publicRepos = input.publicRepos,
            isFavorite = input.isFavorite
        )

    fun mapDomainToEntity(input: User) = UserEntity(
        id = input.id,
        login = input.login,
        avatarUrl = input.avatarUrl,
        name = input.name,
        followers = input.followers,
        following = input.following,
        company = input.company,
        location = input.location,
        publicRepos = input.publicRepos,
        isFavorite = input.isFavorite
    )
}