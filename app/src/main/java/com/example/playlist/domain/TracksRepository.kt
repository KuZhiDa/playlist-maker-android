package com.example.playlist.domain

import com.example.playlist.data.network.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}