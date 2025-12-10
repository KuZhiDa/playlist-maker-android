package com.example.playlist.ui.activity.playlist

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playlist.R
import com.example.playlist.data.model.Playlist
import com.example.playlist.ui.item.PlaylistListItem
import com.example.playlist.ui.viewModel.PlaylistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsScreen(
    modifier: Modifier = Modifier,
    addNewPlaylist: () -> Unit,
    onNavigateBack: () -> Unit,
    navigateToPlaylist: (Long) -> Unit,
    viewModel: PlaylistViewModel = viewModel()
) {

    val playlists by viewModel.playlists.collectAsState(initial = emptyList())
    var selectedToMerge by remember { mutableStateOf<Playlist?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.mergeMessage.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

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
                    contentDescription = stringResource(R.string.back),
                    tint = Color(0xFF1A1B22),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(onClick = onNavigateBack)
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = stringResource(R.string.menu_playlists),
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
                        onClick = { navigateToPlaylist(playlist.id) },
                        onLongClick = { selectedToMerge = playlist }
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
                contentDescription = stringResource(R.string.add_to_playlist)
            )
        }
    }

    val otherPlaylists = playlists.filter { it.id != selectedToMerge?.id }

    if (selectedToMerge != null && otherPlaylists.isNotEmpty()) {
        ModalBottomSheet(
            onDismissRequest = { selectedToMerge = null }
        ) {
            otherPlaylists.forEach { other ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.mergePlaylists(
                                from = selectedToMerge!!,
                                to = other
                            )
                            selectedToMerge = null
                        }
                        .padding(16.dp)
                ) {
                    Text(text = other.name, fontSize = 18.sp)
                }
            }
        }
    }
}
