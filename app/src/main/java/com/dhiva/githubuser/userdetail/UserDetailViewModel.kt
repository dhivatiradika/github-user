package com.dhiva.githubuser.userdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhiva.githubuser.core.domain.model.User
import com.dhiva.githubuser.core.domain.usecase.UserUseCase

class UserDetailViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    private val username: MutableLiveData<String> = MutableLiveData()

    val user = Transformations.switchMap(username) {userUseCase.getUser(it).asLiveData()}

    val followers = Transformations.switchMap(username) {userUseCase.getFollowers(it, 1).asLiveData()}

    val following = Transformations.switchMap(username) {userUseCase.getFollowing(it, 1).asLiveData()}

    val favoriteUsers = userUseCase.getFavoriteUser().asLiveData()

    fun setFavorite(user: User, state: Boolean){
        userUseCase.setFavoriteUser(user, state)
    }

    fun setUsername(username: String){
        this.username.value = username
    }

}