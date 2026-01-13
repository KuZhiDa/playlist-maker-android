package com.example.playlist.data.local.database.mapers
import com.example.playlist.data.model.Track
import com.example.playlist.data.local.database.track.TrackEntity


fun TrackEntity.toTrack(): Track {
    return Track(
        id = this.id,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = this.trackTime,
        favorite = this.favorite,
        artworkUrl = this.image,
        playlistId = this.playlistId
    )
}

fun Track.toEntity(): TrackEntity {
    return TrackEntity(
        id = this.id,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = this.trackTime,
        image = this.artworkUrl,
        favorite = this.favorite,
        playlistId = this.playlistId
    )
}