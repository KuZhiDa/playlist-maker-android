package com.example.playlist.data.dto

import com.example.playlist.domain.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val preferences: SearchHistoryPreferences
) : SearchHistoryRepository {

    override suspend fun getHistory(): List<String> {
        return preferences.getEntries()
    }

    override suspend fun addEntrySuspending(query: String) {
        preferences.addEntrySuspending(query)
    }
}