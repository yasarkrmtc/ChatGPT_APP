package com.yasarkiremitci.chatgbt



import android.app.Application
import androidx.room.Database
import androidx.room.Room
import com.yasarkiremitci.chatgbt.api.ApiInterface
import com.yasarkiremitci.chatgbt.database.AppDatabase
import com.yasarkiremitci.chatgbt.database.ChatMessageDao
import com.yasarkiremitci.chatgbt.repository.ChatRepository
import com.yasarkiremitci.chatgbt.viewModel.ChatViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApp:  Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }

    //koin dependency injection

    private val appModule = module {
        single { provideDatabase() }
        single { provideRetrofit() }
        single { provideApiService(get()) }
        single { provideChatMessageDao(get()) }
        single { ChatRepository(get(),get()) }

        viewModel { ChatViewModel(get()) }
    }
    private fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "chat_database"
        ).build()
    }
    private fun provideApiService(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun provideChatMessageDao(database: AppDatabase): ChatMessageDao {
        return database.chatMessageDao()
    }

}