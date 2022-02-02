package com.dhiva.githubuser

import android.app.Application
import com.dhiva.githubuser.di.useCaseModule
import com.dhiva.githubuser.di.viewModelModule
import com.dhiva.githubuser.core.di.databaseModule
import com.dhiva.githubuser.core.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import com.dhiva.githubuser.core.di.preferencesModule
import com.dhiva.githubuser.core.di.repositoryModule

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                    preferencesModule
                )
            )
        }
    }
}