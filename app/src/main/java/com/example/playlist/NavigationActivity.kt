package com.example.playlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.compose.material3.MaterialTheme

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
            SearchScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(PlaylistScreen.Settings.route) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
