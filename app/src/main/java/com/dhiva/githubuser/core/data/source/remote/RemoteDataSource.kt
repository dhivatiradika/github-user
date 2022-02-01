package com.dhiva.githubuser.core.data.source.remote

import com.dhiva.githubuser.core.data.source.remote.network.ApiResponse
import com.dhiva.githubuser.core.data.source.remote.network.ApiService
import com.dhiva.githubuser.core.data.source.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource private constructor(private val apiService: ApiService){
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(service)
            }
    }

    suspend fun searchUser(query: String): Flow<ApiResponse<List<UserResponse>>>{
        return flow {
            try {
                val response = apiService.searchUser(query)
                val dataArray = response.items
                if (dataArray.isNotEmpty()){
                    emit(ApiResponse.Success(response.items))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getUser(username: String): Flow<ApiResponse<UserResponse>>{
        return flow {
            try {
                val response = apiService.getUser(username)
                emit(ApiResponse.Success(response))
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUsers(): Flow<ApiResponse<List<UserResponse>>>{
        return flow {
            try {
                val response = apiService.getUsers()
                if (response.isNotEmpty()){
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getFollowers(username: String, page: Int): Flow<ApiResponse<List<UserResponse>>>{
        return flow {
            try {
                val response = apiService.getFollowers(username, page)
                if (response.isNotEmpty()){
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getFollowing(username: String, page: Int): Flow<ApiResponse<List<UserResponse>>>{
        return flow {
            try {
                val response = apiService.getFollowing(username, page)
                if (response.isNotEmpty()){
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}