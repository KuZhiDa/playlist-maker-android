package com.example.playlist.data.remote.api

import com.example.playlist.data.remote.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesInterface {
    @GET("search")
    suspend fun searchTracks(
        @Query("term") term: String,
        @Query("entity") entity: String = "song",
        @Query("country") country: String = "ru",
        @Query("limit") limit: Int = 50
    ): TracksSearchResponse
}