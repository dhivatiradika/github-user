package com.dhiva.githubuser.core.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dhiva.githubuser.core.di.Injection
import com.dhiva.githubuser.core.domain.usecase.UserUseCase
import com.dhiva.githubuser.core.utils.SettingPreferences
import com.dhiva.githubuser.favorite.FavoriteViewModel
import com.dhiva.githubuser.home.MainViewModel
import com.dhiva.githubuser.userdetail.UserDetailViewModel

class ViewModelFactory private constructor(private val userUseCase: UserUseCase, private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideUserUseCase(context),
                    Injection.providePreferences(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userUseCase, pref) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(userUseCase) as T
            }
            modelClass.isAssignableFrom(UserDetailViewModel::class.java) -> {
                UserDetailViewModel(userUseCase) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}