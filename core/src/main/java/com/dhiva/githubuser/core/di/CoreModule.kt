package com.dhiva.githubuser.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.dhiva.githubuser.core.data.source.local.LocalDataSource
import com.dhiva.githubuser.core.data.source.local.room.UserDatabase
import com.dhiva.githubuser.core.data.source.remote.RemoteDataSource
import com.dhiva.githubuser.core.data.source.remote.network.ApiService
import com.dhiva.githubuser.core.domain.repository.IUserRepository
import com.dhiva.githubuser.core.utils.AppExecutors
import com.dhiva.githubuser.core.utils.SettingPreferences
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val databaseModule = module {
    factory { get<UserDatabase>().userDao() }
    factory { get<UserDatabase>().userAttributesDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("githubuser".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            UserDatabase::class.java, "user_database"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val hostname = "api.github.com"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/azE5Ew0LGsMgkYqiDpYay0olLAS8cxxNGUZ8OJU756k=")
            .add(hostname, "sha256/vnCogm4QYze/Bc9r88xdA6NTQY74p4BAz2w5gxkLG2M=")
            .add(hostname, "sha256/WoiWRyIOVNa9ihaBciRSC7XHjliYS9VwUGOIud4PB18=")
            .build()
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get(), get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IUserRepository> { com.dhiva.githubuser.core.data.UserRepository(get(), get(), get()) }
}

val preferencesModule = module {
    single { SettingPreferences.getInstance(androidContext().dataStore) }
}