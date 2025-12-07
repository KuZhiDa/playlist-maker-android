package com.example.playlist.ui.playlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playlist.R
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            androidx.compose.material3.IconButton(
                onClick = navigateBack
            ) {
                androidx.compose.material3.Icon(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.back),
                    contentDescription = "Назад",
                    tint = Color.Black
                )
            }
        }

        if (playlist != null) {
            val currentPlaylist = playlist!!

            Text(
                text = currentPlaylist.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (currentPlaylist.description.isNotEmpty()) {
                Text(
                    text = currentPlaylist.description,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            } else {
                Text(
                    text = "Нет описания",
                    fontSize = 16.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Text(
                text = "${currentPlaylist.tracks.size} треков",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            androidx.compose.material3.Divider(
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(currentPlaylist.tracks) { track ->
                    TrackListItem(
                        track = track,
                        onClick = {
                            onNavigateToTrack(track.id.toInt())
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
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