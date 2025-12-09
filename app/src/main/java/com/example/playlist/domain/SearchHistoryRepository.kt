package com.example.playlist.domain

interface SearchHistoryRepository {
    suspend fun getHistory(): List<String>
    suspend fun addEntrySuspending(query: String)
}