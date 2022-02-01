package com.dhiva.githubuser.home

import androidx.lifecycle.*
import com.dhiva.githubuser.core.domain.usecase.UserUseCase
import com.dhiva.githubuser.core.utils.SettingPreferences
import kotlinx.coroutines.launch


class MainViewModel(private val userUseCase: UserUseCase, private val pref: SettingPreferences) : ViewModel() {

    val query: MutableLiveData<String> = MutableLiveData()
    val users = Transformations.switchMap(query) {
        userUseCase.searchUser(it).asLiveData()
    }

    val allUsers = userUseCase.getUsers().asLiveData()

    fun searchUser(query: String) {
        this.query.value = query
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

}