package com.example.playlist.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.playlist.R
import com.example.playlist.data.database.PlaylistsRepositoryImpl
import com.example.playlist.data.dto.ITunesInterface
import com.example.playlist.data.dto.SearchHistoryPreferences
import com.example.playlist.data.dto.SearchHistoryRepositoryImpl
import com.example.playlist.data.network.RetrofitNetworkClient
import com.example.playlist.data.network.TracksRepositoryImpl
import com.example.playlist.database.AppDatabase
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
        return SearchHistoryRepositoryImpl(SearchHistoryPreferences(ds))
    }
}
