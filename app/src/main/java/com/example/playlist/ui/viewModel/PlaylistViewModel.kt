package com.example.playlist.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlist.data.model.Track
import com.example.playlist.di.Creator
import com.example.playlist.data.model.Playlist
import com.example.playlist.domain.PlaylistsRepository
import com.example.playlist.domain.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val tracksRepository: TracksRepository,
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {


    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()

    private val _selectedTrack = MutableStateFlow<Track?>(null)
    val selectedTrack: StateFlow<Track?> = _selectedTrack

    private val _mergeMessage = MutableSharedFlow<String>()
    val mergeMessage = _mergeMessage.asSharedFlow()


    fun setSelectedTrack(track: Track) {
        _selectedTrack.value = track
    }

    fun updateCover(playlistId: Long, coverPath: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.updatePlaylistCover(playlistId, coverPath)
        }
    }
    fun removeSelectedTrack() {
        _selectedTrack.value = null
    }

    val favoriteTracks: StateFlow<List<Track>> = tracksRepository
        .getFavoriteTracks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertSongToPlaylist(track: Track, playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.insertTrackToPlaylist(track, playlistId)
        }
    }


    fun toggleFavorite(track: Track, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.updateTrackFavoriteStatus(track, isFavorite)
        }
    }

    fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return playlistsRepository.getPlaylist(playlistId)
    }


    fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return tracksRepository.getTrackByNameAndArtist(track)
    }

    fun mergePlaylists(from: Playlist, to: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            from.tracks.forEach { track ->
                tracksRepository.insertTrackToPlaylist(track, to.id)
            }
            playlistsRepository.deletePlaylistById(from.id)
            _mergeMessage.emit("${from.name} слит с ${to.name}")
        }
    }


    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PlaylistViewModel(Creator.getTracksRepository(),
                    Creator.getPlaylistsRepository()) as T
            }
        }
    }

}