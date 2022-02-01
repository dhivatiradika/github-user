package com.dhiva.githubuser.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dhiva.githubuser.core.data.UserRepository
import com.dhiva.githubuser.core.data.source.local.LocalDataSource
import com.dhiva.githubuser.core.data.source.local.room.UserDatabase
import com.dhiva.githubuser.core.data.source.remote.RemoteDataSource
import com.dhiva.githubuser.core.data.source.remote.network.ApiConfig
import com.dhiva.githubuser.core.domain.repository.IUserRepository
import com.dhiva.githubuser.core.domain.usecase.UserInteractor
import com.dhiva.githubuser.core.domain.usecase.UserUseCase
import com.dhiva.githubuser.core.utils.AppExecutors
import com.dhiva.githubuser.core.utils.SettingPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {
    private fun provideRepository(context: Context): IUserRepository {
        val database = UserDatabase.getDatabase(context)

        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.getApiService())
        val localDataSource = LocalDataSource.getInstance(database.userDao(), database.userAttributesDao())
        val appExecutors = AppExecutors()

        return UserRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }

    fun provideUserUseCase(context: Context): UserUseCase {
        val repository = provideRepository(context)
        return UserInteractor(repository)
    }

    fun providePreferences(context: Context): SettingPreferences{
        return SettingPreferences.getInstance(context.dataStore)
    }
}