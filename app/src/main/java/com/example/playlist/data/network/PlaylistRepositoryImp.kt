package com.example.playlist.data.database

import com.example.playlist.database.mapers.toPlaylist
import com.example.playlist.database.mapers.toTrack
import com.example.playlist.database.playlist.PlaylistEntity
import com.example.playlist.database.playlist.PlaylistsDao
import com.example.playlist.database.track.TracksDao
import com.example.playlist.domain.Playlist
import com.example.playlist.domain.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val playlistsDao: PlaylistsDao,
    private val tracksDao: TracksDao
) : PlaylistsRepository {

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return combine(
            playlistsDao.getPlaylist(playlistId),
            tracksDao.getTracksByPlaylist(playlistId)
        ) { playlistEntity, tracks ->
            playlistEntity?.toPlaylist(tracks.map { it.toTrack() })
        }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistsDao.getAllPlaylists().combine(
            tracksDao.getAllTracks()
        ) { playlists, tracks ->
            playlists.map { playlist ->
                playlist.toPlaylist(tracks.filter { it.playlistId == playlist.id }.map { it.toTrack() })
            }
        }
    }


    override suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String?) {
        playlistsDao.insertPlaylist(
            PlaylistEntity(
                name = name,
                description = description,
                coverImageUri = coverImageUri
            )
        )
    }


    override suspend fun deletePlaylistById(id: Long) {
        playlistsDao.deletePlaylistById(id)
    }
}
