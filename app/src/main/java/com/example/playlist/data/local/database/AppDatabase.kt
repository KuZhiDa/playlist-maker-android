package com.example.playlist.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.playlist.data.local.database.playlist.PlaylistEntity
import com.example.playlist.data.local.database.playlist.PlaylistsDao
import com.example.playlist.data.local.database.track.TracksDao
import com.example.playlist.data.local.database.track.TrackEntity

@Database(
    entities = [
        TrackEntity::class,
        PlaylistEntity::class
    ], version = 5, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tracksDao(): TracksDao
    abstract fun playlistsDao(): PlaylistsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "playlist_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
