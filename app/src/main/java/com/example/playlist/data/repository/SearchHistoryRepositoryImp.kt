package com.example.playlist.data.repository

import com.example.playlist.data.local.preferences.SearchHistoryPreferences
import com.example.playlist.domain.SearchHistoryRepository

class SearchHistoryRepositoryImp(
    private val preferences: SearchHistoryPreferences
) : SearchHistoryRepository {

    override suspend fun getHistory(): List<String> {
        return preferences.getEntries()
    }

    override suspend fun addEntrySuspending(query: String) {
        preferences.addEntrySuspending(query)
    }
}