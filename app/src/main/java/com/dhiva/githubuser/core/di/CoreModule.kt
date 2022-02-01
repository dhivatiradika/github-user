import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.dhiva.githubuser.core.data.UserRepository
import com.dhiva.githubuser.core.data.source.local.LocalDataSource
import com.dhiva.githubuser.core.data.source.local.room.UserDatabase
import com.dhiva.githubuser.core.data.source.remote.RemoteDataSource
import com.dhiva.githubuser.core.data.source.remote.network.ApiService
import com.dhiva.githubuser.core.domain.repository.IUserRepository
import com.dhiva.githubuser.core.utils.AppExecutors
import com.dhiva.githubuser.core.utils.SettingPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val databaseModule = module {
    factory { get<UserDatabase>().userDao() }
    factory { get<UserDatabase>().userAttributesDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            UserDatabase::class.java, "user_database"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get(), get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IUserRepository> { UserRepository(get(), get(), get()) }
}

val preferencesModule = module {
    single { SettingPreferences.getInstance(androidContext().dataStore) }
}