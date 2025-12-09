package com.example.playlist.data.network

import com.example.playlist.data.dto.TracksSearchRequest
import com.example.playlist.data.dto.TracksSearchResponse
import com.example.playlist.database.AppDatabase
import com.example.playlist.database.mapers.toEntity
import com.example.playlist.database.mapers.toTrack
import com.example.playlist.domain.NetworkClient
import com.example.playlist.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlin.String

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    database: AppDatabase
) : TracksRepository {

    private val dao = database.tracksDao()

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return if (response is TracksSearchResponse) {
            response.results.mapNotNull { dto ->
                val name = dto.trackName?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
                val artist = dto.artistName?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
                val artwork = dto.artworkUrl100?.replace("100x100bb", "500x500bb") ?: ""
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
                    artworkUrl = artwork
                )
            }
        } else {
            emptyList()
        }
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return dao.getTrackByNameAndArtist(track.trackName, track.artistName).map { it?.toTrack() }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return dao.getFavoriteTracks().map { list -> list.map { it.toTrack() } }
    }

    override fun getTrackById(trackId: Long): Flow<Track?> {
        return dao.getTrackById(trackId).map { it?.toTrack() }
    }


    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        val existing = dao.getTrackByNameAndArtist(track.trackName, track.artistName).firstOrNull()
        val updated = (existing?.toTrack() ?: track).copy(
            id = existing?.id ?: track.id,
            playlistId = playlistId
        )
        dao.insertTrack(updated.toEntity())
    }


    override suspend fun deleteTrackFromPlaylist(track: Track) {
        dao.insertTrack(track.copy(playlistId = 0).toEntity())
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        val existing = dao.getTrackByNameAndArtist(track.trackName, track.artistName).firstOrNull()
        val updated = (existing?.toTrack() ?: track).copy(
            id = existing?.id ?: track.id,
            favorite = isFavorite
        )
        dao.insertTrack(updated.toEntity())
    }

}

