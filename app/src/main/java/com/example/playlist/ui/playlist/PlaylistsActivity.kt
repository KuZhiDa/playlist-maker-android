package com.example.playlist.ui.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playlist.R
import com.example.playlist.ui.item.PlaylistListItem
import com.example.playlist.ui.viewModel.PlaylistViewModel

@Composable
fun PlaylistsScreen(
    modifier: Modifier = Modifier,
    addNewPlaylist: () -> Unit,
    onNavigateBack: () -> Unit,
    navigateToPlaylist: (Long) -> Unit,
    viewModel: PlaylistViewModel = viewModel()
) {

    val playlists by viewModel.playlists.collectAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {

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
                    contentDescription = null,
                    tint = Color(0xFF1A1B22),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(onClick = onNavigateBack)
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "Плейлисты",
                    fontSize = 22.sp,
                    color = Color(0xFF1A1B22)
                )
            }

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 4.dp)
            ) {
                items(playlists) { playlist ->
                    PlaylistListItem(
                        playlist = playlist,
                        onClick = { navigateToPlaylist(playlist.id) }
                    )
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .padding(bottom = 31.dp, end = 17.dp)
                .align(Alignment.BottomEnd),
            onClick = { addNewPlaylist() },
            containerColor = Color.LightGray,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(id = R.drawable.is_plus),
                modifier = Modifier.size(24.dp),
                tint = Color.White,
                contentDescription = "Добавить плейлист"
            )
        }
    }
}

