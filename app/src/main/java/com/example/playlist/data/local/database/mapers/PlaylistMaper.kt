package com.example.playlist.data.local.database.mapers

import com.example.playlist.data.model.Track
import com.example.playlist.data.local.database.playlist.PlaylistEntity
import com.example.playlist.data.model.Playlist

fun PlaylistEntity.toPlaylist(tracks: List<Track> = emptyList()): Playlist {
    var image = ""
    if(this.coverImageUri != null){
        image = this.coverImageUri
    }
    return Playlist(
        id = this.id,
        name = this.name,
        description = this.description,
        tracks = tracks,
        coverImageUri = image
    )
}
