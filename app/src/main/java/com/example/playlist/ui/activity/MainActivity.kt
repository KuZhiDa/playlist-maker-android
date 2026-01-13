package com.example.playlist.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist.R
import com.example.playlist.di.Creator
import com.example.playlist.ui.navigation.PlaylistHost
import com.example.playlist.ui.item.MenuItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Creator.initDatabase(applicationContext)
        setContent {
            MaterialTheme {
                PlaylistHost()
            }
        }
    }
}

@Composable
fun MainScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPlaylists: () -> Unit,
    onNavigateToFavorites: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3772E7))
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, top = 14.dp, bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(color = Color.White)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                MenuItem(
                    iconRes = R.drawable.search,
                    title = stringResource(id = R.string.menu_search)
                ) { onNavigateToSearch() }

                MenuItem(
                    iconRes = R.drawable.library,
                    title = stringResource(id = R.string.menu_playlists)
                ) { onNavigateToPlaylists() }

                MenuItem(
                    iconRes = R.drawable.favorite_border,
                    title = stringResource(id = R.string.menu_favorites)
                ) { onNavigateToFavorites() }

                MenuItem(
                    iconRes = R.drawable.settings,
                    title = stringResource(id = R.string.menu_settings)
                ) { onNavigateToSettings() }
            }
        }
    }
}
