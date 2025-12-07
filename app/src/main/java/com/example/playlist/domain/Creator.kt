package com.example.playlist.domain

import com.example.playlist.data.database.DatabaseMock
import kotlinx.coroutines.CoroutineScope
import com.example.playlist.data.network.TracksRepositoryImpl

object Creator {

    private var database: DatabaseMock? = null

    private fun getDatabase(scope: CoroutineScope): DatabaseMock {
        if (database == null) {
            database = DatabaseMock(scope)
        }
        return database!!
    }

    fun getTracksRepository(scope: CoroutineScope): TracksRepository {
        return TracksRepositoryImpl(getDatabase(scope))
    }
}
