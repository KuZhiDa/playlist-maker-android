package com.example.playlist.domain

import com.example.playlist.data.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylist(playlistId: Long): Flow<Playlist?>
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String? = null)
    suspend fun updatePlaylistCover(id: Long, coverImageUri: String?)
    suspend fun deletePlaylistById(id: Long)
}