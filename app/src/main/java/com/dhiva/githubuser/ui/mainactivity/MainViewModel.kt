package com.dhiva.githubuser.ui.mainactivity

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import com.dhiva.githubuser.data.remote.ApiConfig
import com.dhiva.githubuser.data.remote.response.SearchUserResponse
import com.dhiva.githubuser.data.remote.response.User
import com.dhiva.githubuser.utils.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFailure = MutableLiveData<Boolean>()
    val isFailure: LiveData<Boolean> = _isFailure

    private val pref = SettingPreferences.getInstance(application.dataStore)

    companion object{
        private const val TAG = "MainViewModel"
    }

    fun searchUser(query: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(query)
        client.enqueue(object : Callback<SearchUserResponse>{
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _users.value = response.body()?.items
                    _isFailure.value = false
                } else {
                    _isFailure.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isFailure.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })

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