package com.example.playlist.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.playlist.ui.favorites.FavoritesScreen
import com.example.playlist.ui.playlist.CreatePlaylistScreen
import com.example.playlist.ui.playlist.PlaylistsScreen
import com.example.playlist.ui.search.SearchScreen
import com.example.playlist.ui.viewModel.SearchViewModel
import com.example.playlist.ui.settings.SettingsScreen
import com.example.playlist.ui.track.TrackDetailsScreen
import com.example.playlist.ui.viewModel.PlaylistViewModel

enum class PlaylistScreen(val route: String) {
    Main("main"),
    Search("search"),
    Settings("settings"),
    Playlists("playlists"),
    Favorites("favorites"),
    CreatePlaylist("create_playlist"),
    TrackDetail("track_detail")
}

@Composable
fun PlaylistHost() {
    val navController = rememberNavController()
    val playlistViewModel: PlaylistViewModel = viewModel()
    val searchViewModel: SearchViewModel = viewModel(
        factory = SearchViewModel.getViewModelFactory()
    )

    NavHost(
        navController = navController,
        startDestination = PlaylistScreen.Main.route,
        modifier = Modifier.fillMaxSize()
    ) {

        composable(PlaylistScreen.Main.route) {
            MainScreen(
                onNavigateToSearch = { navController.navigate(PlaylistScreen.Search.route) },
                onNavigateToSettings = { navController.navigate(PlaylistScreen.Settings.route) },
                onNavigateToPlaylists = { navController.navigate(PlaylistScreen.Playlists.route) },
                onNavigateToFavorites = { navController.navigate(PlaylistScreen.Favorites.route) }
            )
        }
        composable(PlaylistScreen.Search.route) {
            SearchScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = searchViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTrackDetails = { track ->
                    searchViewModel.setSelectedTrack(track)
                    navController.navigate(PlaylistScreen.TrackDetail.route)
                }
            )
        }
        composable(PlaylistScreen.Settings.route) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(PlaylistScreen.Playlists.route) {
            PlaylistsScreen(
                onNavigateBack = { navController.popBackStack() },
                addNewPlaylist = { navController.navigate(PlaylistScreen.CreatePlaylist.route) },
                viewModel = playlistViewModel
            )
        }
        composable(PlaylistScreen.TrackDetail.route) {
            val track by searchViewModel.selectedTrack.collectAsState()

            track?.let { currentTrack ->
                TrackDetailsScreen(
                    track = currentTrack,
                    onNavigateBack = { navController.popBackStack() },
                    playlistViewModel = playlistViewModel
                )
            }
        }
        composable(PlaylistScreen.CreatePlaylist.route) {
            CreatePlaylistScreen(
                onNavigateBack = { navController.popBackStack() },
                onCreatePlaylist = {name, desc ->
                    playlistViewModel.createPlaylist(name, desc) },
            )
        }
        composable(PlaylistScreen.Favorites.route) {
            FavoritesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTrackDetails = {},
                viewModel = playlistViewModel
            )
        }
    }
}