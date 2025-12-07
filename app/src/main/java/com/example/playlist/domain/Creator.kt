package com.example.playlist.domain

import com.example.playlist.data.database.DatabaseMock
import com.example.playlist.data.dto.ITunesInterface
import com.example.playlist.data.network.RetrofitNetworkClient
import com.example.playlist.data.network.TracksRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ITunesInterface::class.java)
    private val networkClient = RetrofitNetworkClient(apiService)

    fun getTracksRepository(): TracksRepository {
        val database = DatabaseMock(CoroutineScope(Dispatchers.IO))
        return TracksRepositoryImpl(networkClient, database)
    }
}

