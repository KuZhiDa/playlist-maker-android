package com.example.playlist.data.model

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    var tracks: List<Track>,
    var coverImageUri: String
)