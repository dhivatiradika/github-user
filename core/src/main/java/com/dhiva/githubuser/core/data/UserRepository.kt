package com.dhiva.githubuser.core.data

import com.dhiva.githubuser.core.data.source.local.LocalDataSource
import com.dhiva.githubuser.core.data.source.local.entity.UserAttributesEntity
import com.dhiva.githubuser.core.data.source.remote.RemoteDataSource
import com.dhiva.githubuser.core.data.source.remote.network.ApiResponse
import com.dhiva.githubuser.core.data.source.remote.response.UserResponse
import com.dhiva.githubuser.core.domain.model.User
import com.dhiva.githubuser.core.domain.repository.IUserRepository
import com.dhiva.githubuser.core.utils.AppExecutors
import com.dhiva.githubuser.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
) : IUserRepository {

    companion object {
        @Volatile
        private var instance: com.dhiva.githubuser.core.data.UserRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): com.dhiva.githubuser.core.data.UserRepository =
            com.dhiva.githubuser.core.data.UserRepository.Companion.instance ?: synchronized(this) {
                com.dhiva.githubuser.core.data.UserRepository.Companion.instance
                    ?: com.dhiva.githubuser.core.data.UserRepository(
                        remoteData,
                        localData,
                        appExecutors
                    )
            }
    }

    override fun searchUser(query: String): Flow<com.dhiva.githubuser.core.data.Resource<List<User>>> =
        object : com.dhiva.githubuser.core.data.NetworkBoundResource<List<User>, List<UserResponse>>() {
            override fun shouldFetch(data: List<User>?): Boolean = true

            override fun loadFromDB(): Flow<List<User>> {
                return localDataSource.searchUser(query).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> {
                return remoteDataSource.searchUser(query)
            }

            override suspend fun saveCallResult(data: List<UserResponse>) {
                val userList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insert(userList)
            }
        }.asFlow()

    override fun getUsers(): Flow<com.dhiva.githubuser.core.data.Resource<List<User>>> =
        object : com.dhiva.githubuser.core.data.NetworkBoundResource<List<User>, List<UserResponse>>() {
            override fun shouldFetch(data: List<User>?): Boolean = true

            override fun loadFromDB(): Flow<List<User>> {
                return localDataSource.getAllUser().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> {
                return remoteDataSource.getUsers()
            }

            override suspend fun saveCallResult(data: List<UserResponse>) {
                val userList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insert(userList)
            }
        }.asFlow()

    override fun getFavoriteUser(): Flow<List<User>> {
        return localDataSource.getFavoriteUser().map { DataMapper.mapEntitiesToDomain(it) }
    }

    override fun setFavoriteUser(user: User, state: Boolean) {
        val userEntity = DataMapper.mapDomainToEntity(user)
        appExecutors.diskIO().execute {
            localDataSource.setFavorite(userEntity, state)
        }
    }

    override fun getUser(username: String): Flow<com.dhiva.githubuser.core.data.Resource<User>> =
        object : com.dhiva.githubuser.core.data.NetworkBoundResource<User, UserResponse>() {
            override fun loadFromDB(): Flow<User> {
                return localDataSource.getUserByUsername(username).map { DataMapper.mapSingleEntitiesToDomain(it) }
            }
            override fun shouldFetch(data: User?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<UserResponse>> {
                return remoteDataSource.getUser(username)
            }

            override suspend fun saveCallResult(data: UserResponse) {
                val listData = ArrayList<UserResponse>()
                listData.add(data)
                val userList = DataMapper.mapResponsesToEntities(listData)
                localDataSource.insert(userList)
            }

        }.asFlow()

    override fun getFollowers(username: String, page: Int): Flow<com.dhiva.githubuser.core.data.Resource<List<User>>>  =
        object : com.dhiva.githubuser.core.data.NetworkBoundResource<List<User>, List<UserResponse>>() {
            override fun shouldFetch(data: List<User>?): Boolean = true

            override fun loadFromDB(): Flow<List<User>> {
                return localDataSource.getAllFollowers(username).map { DataMapper.mapEntitiesToDomain(it) }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> {
                return remoteDataSource.getFollowers(username, page)
            }

            override suspend fun saveCallResult(data: List<UserResponse>) {
                val userList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insert(userList)

                val dataAtt = data.map {
                    UserAttributesEntity(
                        owner = username,
                        type = 1,
                        userId = it.id
                    )
                }
                localDataSource.insertUserAtt(dataAtt)
            }
        }.asFlow()

    override fun getFollowing(username: String, page: Int): Flow<com.dhiva.githubuser.core.data.Resource<List<User>>>  =
        object : com.dhiva.githubuser.core.data.NetworkBoundResource<List<User>, List<UserResponse>>() {
            override fun shouldFetch(data: List<User>?): Boolean = true

            override fun loadFromDB(): Flow<List<User>> {
                return localDataSource.getAllFollowing(username).map { DataMapper.mapEntitiesToDomain(it) }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> {
                return remoteDataSource.getFollowing(username, page)
            }

            override suspend fun saveCallResult(data: List<UserResponse>) {
                val userList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insert(userList)

                val dataAtt = data.map {
                    UserAttributesEntity(
                        owner = username,
                        type = 2,
                        userId = it.id
                    )
                }
                localDataSource.insertUserAtt(dataAtt)
            }
        }.asFlow()

}