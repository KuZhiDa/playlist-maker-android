package com.example.playlist.ui.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playlist.R
import com.example.playlist.data.network.Track
import com.example.playlist.ui.item.TrackListItem
import com.example.playlist.ui.viewModel.PlaylistViewModel

@Composable
fun PlaylistActivity(
    modifier: Modifier = Modifier,
    playlistViewModel: PlaylistViewModel = viewModel(),
    playlistId: Long,
    navigateBack: () -> Unit,
    onNavigateToTrack: (Int) -> Unit
) {
    val playlist by playlistViewModel.getPlaylist(playlistId).collectAsState(initial = null)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Назад",
                tint = Color(0xFF1A1B22),
                modifier = Modifier
                    .size(16.dp)
                    .clickable { navigateBack() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        playlist?.let { currentPlaylist ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.picha),
                    contentDescription = "Обложка плейлиста",
                    modifier = Modifier
                        .size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .width(384.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = currentPlaylist.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                if (currentPlaylist.description.isNotEmpty()) {
                    Text(
                        text = currentPlaylist.description,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                val totalMinutes = currentPlaylist.tracks.totalMinutes()
                Text(
                    text = "$totalMinutes мин • ${currentPlaylist.tracks.size} треков",
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 4.dp)
                )


                Icon(
                    painter = painterResource(id = R.drawable.three_dots),
                    contentDescription = "Меню",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(currentPlaylist.tracks) { track ->
                    TrackListItem(
                        track = track,
                        onClick = { onNavigateToTrack(track.id.toInt()) }
                    )
                }
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Плейлист не найден",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

fun List<Track>.totalMinutes(): Int {
    return this.sumOf { track ->
        val parts = track.trackTime.split(":").map { it.toIntOrNull() ?: 0 }
        when (parts.size) {
            2 -> parts[0]
            3 -> parts[0] * 60 + parts[1]
            else -> 0
        }
    }
}
