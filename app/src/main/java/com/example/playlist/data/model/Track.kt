package com.example.playlist.data.model

data class Track(
    val id: Long = 0,
    val playlistId: Long = 0,
    val favorite: Boolean = false,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl: String
)