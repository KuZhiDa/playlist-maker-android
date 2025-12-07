package com.example.playlist.data.network

import com.example.playlist.data.database.DatabaseMock
import com.example.playlist.domain.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class TracksRepositoryImpl(
    private val database: DatabaseMock
) : TracksRepository {


    override fun getTrackById(trackId: Long): Flow<Track?> {
        return database.getTrackById(trackId)
    }

    override suspend fun searchTracks(expression: String): List<Track> {
        return database.searchTracks(expression)
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return database.getTrackByNameAndArtist(track)
    }

    override suspend fun insertSongToPlaylist(track: Track, playlistId: Long) {
        val trackWithPlaylist = track.copy(
            playlistId = playlistId,
            id = System.currentTimeMillis()
        )
        database.insertTrack(trackWithPlaylist)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track) {
        database.insertTrack(track.copy(playlistId = 0))
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        database.insertTrack(track.copy(favorite = isFavorite))
    }

    override suspend fun deleteTracksByPlaylistId(playlistId: Long) {
        database.deleteTracksByPlaylistId(playlistId)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database.getFavoriteTracks()
    }


}