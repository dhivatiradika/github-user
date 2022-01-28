package com.dhiva.githubuser.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dhiva.githubuser.core.data.UserRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepository(application)
    val listUser = userRepository.getAllUser()

}