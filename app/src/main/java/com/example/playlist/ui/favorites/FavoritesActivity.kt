package com.example.playlist.ui.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist.R
import com.example.playlist.data.network.Track
import com.example.playlist.ui.item.TrackListItem
import com.example.playlist.ui.viewModel.PlaylistViewModel

@Composable
fun FavoritesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTrackDetails: (Track) -> Unit,
    viewModel: PlaylistViewModel
) {
    val favoriteTracks by viewModel.favoriteTracks.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Назад",
                tint = Color(0xFF1A1B22),
                modifier = Modifier
                    .size(16.dp)
                    .clickable(onClick = onNavigateBack)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = "Избранные треки",
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp,
                color = Color(0xFF1A1B22)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (favoriteTracks.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoriteTracks) { track ->
                    TrackListItem(
                        track = track,
                        onClick = { onNavigateToTrackDetails(track) }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.empty_search),
                        contentDescription = "Нет избранных треков",
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ваша медиатека пуста",
                        fontWeight = FontWeight.Medium,
                        fontSize = 19.sp,
                        color = Color(0xFF000000)
                    )
                }
            }
        }
    }
}