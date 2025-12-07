package com.example.playlist.data.network

import com.example.playlist.data.database.DatabaseMock
import com.example.playlist.data.dto.TracksSearchRequest
import com.example.playlist.data.dto.TracksSearchResponse
import com.example.playlist.domain.NetworkClient
import com.example.playlist.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlin.String

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: DatabaseMock
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        return if (response is TracksSearchResponse) {
            response.results.mapNotNull { dto ->

                val name = dto.trackName?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
                val artist = dto.artistName?.takeIf { it.isNotBlank() } ?: return@mapNotNull null

                val durationMillis = dto.trackTimeMillis ?: 0L
                val minutes = durationMillis / 60000
                val seconds = (durationMillis % 60000) / 1000

                val time = "$minutes:${seconds.toString().padStart(2, '0')}"

                Track(
                    id = dto.trackId ?: (name + artist).hashCode().toLong(),
                    playlistId = 0,
                    favorite = false,
                    trackName = name,
                    artistName = artist,
                    trackTime = time,
                    artworkUrl = dto.artworkUrl100
                )
            }
        } else {
            emptyList()
        }
    }


    override fun getTrackById(trackId: Long): Flow<Track?> {
        return database.getTrackById(trackId)
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return database.getTrackByNameAndArtist(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database.getFavoriteTracks()
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
}
