package com.dhiva.githubuser.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhiva.githubuser.core.domain.usecase.UserUseCase

class FavoriteViewModel(userUseCase: UserUseCase) : ViewModel() {

    val listUser = userUseCase.getFavoriteUser().asLiveData()

}