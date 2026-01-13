package com.example.playlist.ui.activity.playlist

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.playlist.R
import com.example.playlist.data.model.Track
import com.example.playlist.ui.item.TrackListItem
import com.example.playlist.ui.utils.saveCoverToInternalStorage
import com.example.playlist.ui.viewModel.PlaylistViewModel

@Composable
fun PlaylistActivity(
    modifier: Modifier = Modifier,
    playlistViewModel: PlaylistViewModel,
    playlistId: Long,
    navigateBack: () -> Unit,
    onNavigateToTrack: (Track) -> Unit
) {
    val playlist by playlistViewModel.getPlaylist(playlistId).collectAsState(initial = null)
    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val savedPath = saveCoverToInternalStorage(context, it)
            playlistViewModel.updateCover(playlistId, savedPath)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) pickImageLauncher.launch("image/*")
    }

    fun chooseImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickImageLauncher.launch("image/*")
        } else {
            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
            val granted = ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) pickImageLauncher.launch("image/*")
            else permissionLauncher.launch(permission)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
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
                modifier = Modifier
                    .size(16.dp)
                    .clickable { navigateBack() }
            )
        }

        playlist?.let { currentPlaylist ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { chooseImage() },
                    contentAlignment = Alignment.Center
                ) {
                    if (currentPlaylist.coverImageUri.isNotEmpty()) {
                        AsyncImage(
                            model = currentPlaylist.coverImageUri,
                            contentDescription = stringResource(R.string.playlist_cover),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.picha),
                            contentDescription = stringResource(R.string.playlist_cover),
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = currentPlaylist.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (currentPlaylist.description.isNotEmpty()) {
                    Text(
                        text = currentPlaylist.description,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = "${currentPlaylist.tracks.totalMinutes()} ${stringResource(R.string.minutes)} • ${currentPlaylist.tracks.size} ${stringResource(R.string.tracks)}",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(R.drawable.three_dots),
                    contentDescription = stringResource(R.string.playlist_settings),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(currentPlaylist.tracks) { track ->
                        TrackListItem(
                            track = track,
                            onClick = { onNavigateToTrack(track) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.playlist_not_found),
                    fontSize = 16.sp
                )
            }
        }
    }
}

fun List<Track>.totalMinutes(): Int {
    return this.sumOf { track ->
        val parts = track.trackTime.split(":").map { it.toIntOrNull() ?: 0 }
        when (parts.size) {
            2 -> parts[0] + if (parts[1] >= 30) 1 else 0
            3 -> parts[0] * 60 + parts[1] + if (parts[2] >= 30) 1 else 0
            else -> 0
        }
    }
}
