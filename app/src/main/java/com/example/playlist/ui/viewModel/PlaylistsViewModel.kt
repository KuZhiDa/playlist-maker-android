package com.example.playlist.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.data.database.DatabaseMock
import com.example.playlist.data.database.PlaylistsRepositoryImpl
import com.example.playlist.data.network.Track
import com.example.playlist.data.network.TracksRepositoryImpl
import com.example.playlist.domain.Creator
import com.example.playlist.domain.Playlist
import com.example.playlist.domain.PlaylistsRepository
import com.example.playlist.domain.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel() : ViewModel() {
    private val tracksRepository: TracksRepository = Creator.getTracksRepository()

    private val database = DatabaseMock(scope = CoroutineScope(Dispatchers.IO))

    private val playlistsRepository: PlaylistsRepository = PlaylistsRepositoryImpl(database)

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()
    val favoriteTracks: StateFlow<List<Track>> = tracksRepository
        .getFavoriteTracks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )



    fun createPlaylist(name: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addNewPlaylist(name, description)
        }
    }

    fun insertSongToPlaylist(track: Track, playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.insertSongToPlaylist(track, playlistId)
        }
    }

    fun toggleFavorite(track: Track, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.updateTrackFavoriteStatus(track, isFavorite)
        }
    }

    fun getTrackFlow(trackId: Long) = tracksRepository.getTrackById(trackId)

    fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return playlistsRepository.getPlaylist(playlistId)
    }
}