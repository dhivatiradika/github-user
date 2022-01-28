package com.dhiva.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dhiva.githubuser.data.UserRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepository(application)
    val listUser = userRepository.getAllUser()

}