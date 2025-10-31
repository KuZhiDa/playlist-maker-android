package com.example.playlist.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.playlist.ui.search.SearchScreen
import com.example.playlist.ui.search.SearchViewModel
import com.example.playlist.ui.settings.SettingsScreen

enum class PlaylistScreen(val route: String) {
    Main("main"),
    Search("search"),
    Settings("settings")
}

@Composable
fun PlaylistHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PlaylistScreen.Main.route
    ) {
        composable(PlaylistScreen.Main.route) {
            MainScreen(
                onNavigateToSearch = { navController.navigate(PlaylistScreen.Search.route) },
                onNavigateToSettings = { navController.navigate(PlaylistScreen.Settings.route) }
            )
        }
        composable(PlaylistScreen.Search.route) {
            val searchViewModel: SearchViewModel = viewModel(
                factory = SearchViewModel.getViewModelFactory()
            )

            SearchScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = searchViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(PlaylistScreen.Settings.route) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
