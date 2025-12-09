package com.example.playlist.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlist.data.network.Track
import com.example.playlist.domain.Creator
import com.example.playlist.domain.SearchHistoryRepository
import com.example.playlist.domain.TracksRepository
import com.example.playlist.ui.search.SearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(
    private val tracksRepository: TracksRepository,
    private val historyRepository: SearchHistoryRepository
) : ViewModel() {
    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState  = _searchScreenState.asStateFlow()
    private val _selectedTrack = MutableStateFlow<Track?>(null)
    val selectedTrack  = _selectedTrack.asStateFlow()

    private val _currentQuery = MutableStateFlow("")
    val currentQuery = _currentQuery.asStateFlow()

    fun setCurrentQuery(query: String) {
        _currentQuery.value = query
    }

    fun resetAll() {
        _currentQuery.value = ""
        resetSearch()
    }

    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history = _history.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _history.value = historyRepository.getHistory()
        }
    }

    fun search(whatSearch: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (whatSearch.isBlank()) {
                    _searchScreenState.update { SearchState.Initial }
                    return@launch
                }

                historyRepository.addEntrySuspending(whatSearch)
                _history.value = historyRepository.getHistory()

                _searchScreenState.update { SearchState.Searching }
                val list = tracksRepository.searchTracks(expression = whatSearch)
                _searchScreenState.update { SearchState.Success(list = list) }
            } catch (e: IOException){
                _searchScreenState.update { SearchState.Fail(e.message.toString()) }
            }
        }
    }



    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(
                        Creator.getTracksRepository(),
                        Creator.getSearchHistoryRepository()
                    ) as T
                }
            }
    }

    fun setSelectedTrack(track: Track) {
        _selectedTrack.value = track
    }

    fun resetSearch() {
        _searchScreenState.value = SearchState.Initial
    }

}