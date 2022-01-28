package com.dhiva.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.dhiva.githubuser.data.local.entity.UserEntity
import com.dhiva.githubuser.data.local.room.UserDao
import com.dhiva.githubuser.data.local.room.UserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val userDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserDatabase.getDatabase(application)
        userDao = db.userDao()
    }

    fun getAllUser(): LiveData<List<UserEntity>> = userDao.getAllUser()

    fun getUserByUsername(username: String): LiveData<UserEntity>? = userDao.getUserByUsername(username)

    fun insert(user: UserEntity){
        executorService.execute { userDao.insert(user) }
    }

    fun delete(user: UserEntity){
        executorService.execute { userDao.delete(user) }
    }
}