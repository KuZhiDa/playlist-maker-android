package com.example.playlist.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class SearchHistoryPreferences(
    private val dataStore: DataStore<Preferences>,
) {

    suspend fun addEntrySuspending(word: String) {
        if (word.isEmpty()) return

        dataStore.updateData { preferences ->
            val historyString = preferences[preferencesKey].orEmpty()
            val history = if (historyString.isNotEmpty()) {
                historyString.split(SEPARATOR).toMutableList()
            } else {
                mutableListOf()
            }

            history.remove(word)
            history.add(0, word)

            val updated = history.take(MAX_ENTRIES).joinToString(SEPARATOR)

            preferences.toMutablePreferences().apply {
                this[preferencesKey] = updated
            }
        }
    }


    suspend fun getEntries(): List<String> {
        val prefs = dataStore.data.first()
        val historyString = prefs[preferencesKey].orEmpty()
        return if (historyString.isNotEmpty()) {
            historyString.split(SEPARATOR)
        } else {
            emptyList()
        }
    }

    companion object {
        private const val SEPARATOR = ","
        private const val MAX_ENTRIES = 10
        val preferencesKey = stringPreferencesKey("search_history")
    }
}