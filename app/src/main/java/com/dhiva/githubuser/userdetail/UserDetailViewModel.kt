package com.dhiva.githubuser.userdetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhiva.githubuser.core.data.UserRepository
import com.dhiva.githubuser.core.data.source.local.entity.UserEntity
import com.dhiva.githubuser.core.data.source.remote.ApiConfig
import com.dhiva.githubuser.core.data.source.remote.response.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository = UserRepository(application)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _followers = MutableLiveData<List<User>>()
    val followers: LiveData<List<User>> = _followers

    private val _following = MutableLiveData<List<User>>()
    val following: LiveData<List<User>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingFollowers = MutableLiveData<Boolean>()
    val isLoadingFollowers: LiveData<Boolean> = _isLoadingFollowers

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    private val _isFailure = MutableLiveData<Boolean>()
    val isFailure: LiveData<Boolean> = _isFailure

    private val _isFollowingFragmentCreated = MutableLiveData<Boolean>()
    val isFollowingFragmentCreated: LiveData<Boolean> = _isFollowingFragmentCreated

    fun getUser(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(username)
        client.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _user.value = response.body()
                    _isFailure.value = false
                } else {
                    _isFailure.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isLoading.value = false
                _isFailure.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getFollowers(username: String, page: Int){
        _isLoadingFollowers.value = true
        val client = ApiConfig.getApiService().getFollowers(username, page)
        client.enqueue(object : Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoadingFollowers.value = false
                if (response.isSuccessful){
                    _followers.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoadingFollowers.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getFollowing(username: String, page: Int){
        _isLoadingFollowing.value = true
        val client = ApiConfig.getApiService().getFollowing(username, page)
        client.enqueue(object : Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful){
                    _following.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoadingFollowing.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun setIsFollowingFragmentCreated(value: Boolean){
        _isFollowingFragmentCreated.value = value
    }

    fun insertToFavorite(user: User){
        val userEntity = UserEntity(
            login = user.login,
            avatarUrl = user.avatarUrl,
            name = user.name,
            followers = user.followers,
            following = user.following,
            company = user.company,
            location = user.location,
            publicRepos = user.publicRepos,
        )
        userRepository.insert(userEntity)
    }

    fun getUserByUsername(username: String) : LiveData<UserEntity>? {
        return userRepository.getUserByUsername(username)
    }

    fun deleteFavorite(user: UserEntity){
        userRepository.delete(user)
    }

    companion object{
        private const val TAG = "UserDetailViewModel"
    }

}