package com.example.playlist.domain

import com.example.playlist.creator.Storage
import com.example.playlist.data.network.RetrofitNetworkClient
import com.example.playlist.data.network.TracksRepositoryImpl

object Creator {
    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(Storage()))
    }
}