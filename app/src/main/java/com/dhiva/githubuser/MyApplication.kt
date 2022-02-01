package com.dhiva.githubuser

import android.app.Application
import com.dhiva.githubuser.di.useCaseModule
import com.dhiva.githubuser.di.viewModelModule
import databaseModule
import networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import preferencesModule
import repositoryModule

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