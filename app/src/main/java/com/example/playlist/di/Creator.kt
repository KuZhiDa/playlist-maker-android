package com.example.playlist.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.playlist.R
import com.example.playlist.data.repository.PlaylistsRepositoryImpl
import com.example.playlist.data.remote.api.ITunesInterface
import com.example.playlist.data.local.preferences.SearchHistoryPreferences
import com.example.playlist.data.remote.network.RetrofitNetworkClient
import com.example.playlist.data.repository.TracksRepositoryImpl
import com.example.playlist.data.local.database.AppDatabase
import com.example.playlist.data.repository.SearchHistoryRepositoryImp
import com.example.playlist.domain.PlaylistsRepository
import com.example.playlist.domain.SearchHistoryRepository
import com.example.playlist.domain.TracksRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history")


object Creator {

    @Volatile
    private var database: AppDatabase? = null

    @Volatile
    private var dataStore: DataStore<Preferences>? = null

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ITunesInterface by lazy {
        retrofit.create(ITunesInterface::class.java)
    }

    private val networkClient: RetrofitNetworkClient by lazy {
        RetrofitNetworkClient(apiService)
    }

    fun initDatabase(context: Context) {
        if (database == null) {
            synchronized(this) {
                if (database == null) {
                    database = AppDatabase.getInstance(context)
                }
            }
        }
        if (dataStore == null) {
            dataStore = context.dataStore
        }
    }

    private fun getDatabase(): AppDatabase {
        return database ?: throw IllegalStateException(R.string.database_not_init.toString())
    }

    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            networkClient = networkClient,
            database = getDatabase()
        )
    }


    fun getPlaylistsRepository(): PlaylistsRepository {
        return PlaylistsRepositoryImpl(getDatabase().playlistsDao(), tracksDao = getDatabase().tracksDao())
    }

    fun getSearchHistoryRepository(): SearchHistoryRepository {
        val ds = dataStore ?: error("DataStore not initialized, call Creator.initDatabase(ctx)")
        return SearchHistoryRepositoryImp(SearchHistoryPreferences(ds))
    }
}
