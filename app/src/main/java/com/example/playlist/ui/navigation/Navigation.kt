package com.example.playlist.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.playlist.ui.activity.MainScreen
import com.example.playlist.ui.activity.favorites.FavoritesScreen
import com.example.playlist.ui.activity.playlist.CreatePlaylistScreen
import com.example.playlist.ui.activity.playlist.PlaylistActivity
import com.example.playlist.ui.activity.playlist.PlaylistsScreen
import com.example.playlist.ui.activity.search.SearchScreen
import com.example.playlist.ui.viewModel.SearchViewModel
import com.example.playlist.ui.activity.settings.SettingsScreen
import com.example.playlist.ui.activity.track.TrackDetailsScreen
import com.example.playlist.ui.viewModel.NewPlaylistViewModel
import com.example.playlist.ui.viewModel.PlaylistViewModel

enum class PlaylistScreen(val route: String) {
    Main("main"),
    Search("search"),
    Settings("settings"),
    Playlists("playlists"),
    Playlist("playlist"),
    Favorites("favorites"),
    CreatePlaylist("create_playlist"),
    TrackDetail("track_detail")
}

@Composable
fun PlaylistHost() {
    val navController = rememberNavController()
    val playlistViewModel: PlaylistViewModel = viewModel(
        factory = PlaylistViewModel.factory
    )
    val searchViewModel: SearchViewModel = viewModel(
        factory = SearchViewModel.getViewModelFactory()
    )
    val newPlaylistViewModel: NewPlaylistViewModel = viewModel(
        factory = NewPlaylistViewModel.factory
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
                searchViewModel = searchViewModel,
                playlistViewModel = playlistViewModel,
                onNavigateBack = {
                    searchViewModel.resetAll()
                    navController.popBackStack()
                },
                onNavigateToTrackDetails = { navController.navigate(PlaylistScreen.TrackDetail.route) }
            )
        }
        composable(PlaylistScreen.Settings.route) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(PlaylistScreen.Playlists.route) {
            PlaylistsScreen(
                onNavigateBack = { navController.popBackStack() },
                addNewPlaylist = { navController.navigate(PlaylistScreen.CreatePlaylist.route) },
                navigateToPlaylist = { playlistId ->
                    navController.navigate("${PlaylistScreen.Playlist.route}/$playlistId")
                },
                viewModel = playlistViewModel
            )
        }

        composable(
            route = "${PlaylistScreen.Playlist.route}/{playlistId}",
            arguments = listOf(
                navArgument("playlistId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: 0L
            PlaylistActivity(
                modifier = Modifier,
                playlistViewModel = playlistViewModel,
                playlistId = playlistId,
                onNavigateToTrack = { track ->
                    playlistViewModel.setSelectedTrack(track)
                    navController.navigate(PlaylistScreen.TrackDetail.route)
                },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(PlaylistScreen.TrackDetail.route) {
            val currentTrack by playlistViewModel.selectedTrack.collectAsState()
            currentTrack?.let { track ->
                TrackDetailsScreen(
                    track = track,
                    onNavigateBack = {
                        playlistViewModel.removeSelectedTrack()
                        navController.popBackStack()
                    },
                    playlistViewModel = playlistViewModel
                )
            }
        }


        composable(PlaylistScreen.CreatePlaylist.route) {
            CreatePlaylistScreen(
                viewModel = newPlaylistViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(PlaylistScreen.Favorites.route) {
            FavoritesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTrackDetails = { track ->
                    playlistViewModel.setSelectedTrack(track)
                    navController.navigate(PlaylistScreen.TrackDetail.route)
                },
                viewModel = playlistViewModel
            )
        }
    }
}