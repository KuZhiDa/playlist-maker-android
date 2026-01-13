package com.example.playlist.ui.activity.track

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlist.R
import com.example.playlist.data.model.Track
import com.example.playlist.ui.viewModel.PlaylistViewModel
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    track: Track,
    onNavigateBack: () -> Unit,
    playlistViewModel: PlaylistViewModel
) {
    val dbTrackFlow = remember(track) { playlistViewModel.getTrackByNameAndArtist(track) }
    val dbTrack by dbTrackFlow.collectAsState(initial = null)
    val currentTrack = dbTrack ?: track
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val playlists by playlistViewModel.playlists.collectAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        playlistViewModel.removeSelectedTrack()
                        onNavigateBack()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = stringResource(id = R.string.back),
                    tint = Color(0xFF1A1B22),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(312.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = track.artworkUrl,
                contentDescription = track.trackName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(312.dp),
                placeholder = painterResource(R.drawable.ic_music),
                error = painterResource(R.drawable.ic_music),
                fallback = painterResource(R.drawable.ic_music)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.width(312.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = track.trackName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1B22)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = track.artistName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1B22)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FloatingActionButton(
                    onClick = { showBottomSheet = true },
                    containerColor = Color(0xFFF5F5F5),
                    contentColor = Color(0xFF1A1B22),
                    shape = CircleShape,
                    modifier = Modifier.size(51.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = stringResource(id = R.string.add_to_playlist),
                        modifier = Modifier.size(24.dp)
                    )
                }

                FloatingActionButton(
                    onClick = {
                        currentTrack.let { playlistViewModel.toggleFavorite(it, !it.favorite) }
                    },
                    containerColor = if (currentTrack.favorite) Color(0xFFFF3B30) else Color(0xFFF5F5F5),
                    contentColor = if (currentTrack.favorite) Color.White else Color(0xFF1A1B22),
                    shape = CircleShape,
                    modifier = Modifier.size(51.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.like),
                        contentDescription = if (currentTrack.favorite)
                            stringResource(id = R.string.remove_from_favorites)
                        else
                            stringResource(id = R.string.add_to_favorites),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.duration),
                    fontSize = 13.sp,
                    color = Color(0xFFAEAFB4)
                )
                Text(
                    text = track.trackTime,
                    fontSize = 13.sp,
                    color = Color(0xFF1A1B22),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.add_to_playlist_title),
                    fontSize = 20.sp,
                    color = Color(0xFF1A1B22),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                if (playlists.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_playlists),
                            color = Color(0xFFAEAFB4),
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyColumn {
                        items(playlists) { playlist ->
                            val playlistCoverUri = playlist.coverImageUri.let {
                                Uri.fromFile(File(it))
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        playlistViewModel.insertSongToPlaylist(
                                            currentTrack,
                                            playlist.id
                                        )
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(45.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF5F5F5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = playlistCoverUri ?: R.drawable.ic_music,
                                        contentDescription = stringResource(id = R.string.playlist_cover),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(45.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = playlist.name,
                                        fontSize = 16.sp,
                                        color = Color(0xFF1A1B22),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "${playlist.tracks.size} треков",
                                        fontSize = 12.sp,
                                        color = Color(0xFFAEAFB4)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
