package com.example.playlist.data.database

import com.example.playlist.data.network.Track
import com.example.playlist.domain.Playlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DatabaseMock(
    private val scope: CoroutineScope,
) {
    private val _historyUpdates = MutableSharedFlow<Unit>()
    private val playlists = mutableListOf<Playlist>()
    private val tracks = mutableListOf<Track>()

    private val _favoriteTracks = MutableStateFlow<List<Track>>(emptyList())
    val favoriteTracksFlow: StateFlow<List<Track>> get() = _favoriteTracks

    init {
        var idCounter = System.currentTimeMillis()
        tracks.addAll(listOf(
            Track(id = idCounter++, trackName = "Владивосток 2000", artistName = "Мумий Троль", trackTime = "02:38", playlistId = 0),
            Track(id = idCounter++, trackName = "Группа крови", artistName = "Кино", trackTime = "04:43", playlistId = 0),
            Track(id = idCounter++, trackName = "Не смотри назад", artistName = "Ария", trackTime = "05:12", playlistId = 0),
            Track(id = idCounter++, trackName = "Звезда по имени Солнце", artistName = "Кино", trackTime = "03:45", playlistId = 0),
            Track(id = idCounter++, trackName = "Лондон", artistName = "Аквариум", trackTime = "04:32", playlistId = 0),
            Track(id = idCounter++, trackName = "На заре", artistName = "Альянс", trackTime = "03:50", playlistId = 0),
            Track(id = idCounter++, trackName = "Перемен", artistName = "Кино", trackTime = "04:56", playlistId = 0),
            Track(id = idCounter++, trackName = "Розовый фламинго", artistName = "Сплин", trackTime = "03:15", playlistId = 0),
            Track(id = idCounter++, trackName = "Танцевать", artistName = "Мельница", trackTime = "03:42", playlistId = 0),
            Track(id = idCounter++, trackName = "Чёрный бумер", artistName = "Серега", trackTime = "04:01", playlistId = 0)
        ))
    }

    fun searchTracks(expression: String): List<Track> {
        return tracks.filter {
            it.trackName.contains(expression, true) ||
                    it.artistName.contains(expression, true)
        }
    }

    private fun notifyHistoryChanged() {
        scope.launch(Dispatchers.IO) {
            _historyUpdates.emit(Unit)
        }
    }

    fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        delay(500)
        val filteredPlaylists = mutableListOf<Playlist>()
        playlists.forEach { playlist ->
            val playlistTracks = tracks.filter { track ->
                track.playlistId == playlist.id
            }
            filteredPlaylists.add(playlist.copy(tracks = playlistTracks))
        }

        emit(filteredPlaylists.toList())
        delay(100)
    }

    fun getPlaylist(id: Long): Flow<Playlist?> = flow {
        emit(playlists.find { it.id == id })
    }

    fun addNewPlaylist(name: String, description: String) {
        playlists.add(
            Playlist(
                id = playlists.size.toLong() + 1,
                name = name,
                description = description,
                tracks = emptyList()
            )
        )
    }

    fun deletePlaylistById(playlistId: Long) {
        playlists.removeIf { it.id == playlistId }
    }

    fun getTrackById(trackId: Long): Flow<Track?> = flow {
        emit(tracks.find { it.id == trackId })
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(tracks.find { it.trackName == track.trackName && it.artistName == track.artistName })
    }

    fun getFavoriteTracks(): Flow<List<Track>> = favoriteTracksFlow

    fun insertTrack(track: Track) {
        val existingIndex = tracks.indexOfFirst {
            it.trackName == track.trackName &&
                    it.artistName == track.artistName &&
                    it.playlistId == track.playlistId
        }

        if (existingIndex != -1) {
            tracks[existingIndex] = track
        } else {
            tracks.add(track)
        }

        _favoriteTracks.value = tracks.filter { it.favorite }
    }

    fun deleteTracksByPlaylistId(playlistId: Long) {
        tracks.removeIf { it.playlistId == playlistId }
    }
}