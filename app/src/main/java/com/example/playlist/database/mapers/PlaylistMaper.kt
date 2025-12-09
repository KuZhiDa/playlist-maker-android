package com.example.playlist.database.mapers

import com.example.playlist.data.network.Track
import com.example.playlist.database.playlist.PlaylistEntity
import com.example.playlist.domain.Playlist

fun PlaylistEntity.toPlaylist(tracks: List<Track> = emptyList()): Playlist {
    return Playlist(
        id = this.id,
        name = this.name,
        description = this.description,
        tracks = tracks
    )
}

fun Playlist.toEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = this.id,
        name = this.name,
        description = this.description
    )
}