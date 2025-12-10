package com.example.playlist.ui.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlist.data.model.Playlist
import com.example.playlist.R
import androidx.core.net.toUri

@Composable
fun PlaylistListItem(
    playlist: Playlist,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (playlist.coverImageUri.isNotEmpty()) {
            AsyncImage(
                model = playlist.coverImageUri.toUri(),
                contentDescription = playlist.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(45.dp).clip(RoundedCornerShape(8.dp))
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.picha),
                contentDescription = playlist.name,
                colorFilter = ColorFilter.tint(Color.Gray),
                modifier = Modifier.size(45.dp).clip(RoundedCornerShape(8.dp))
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = playlist.name,
                fontSize = 16.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.W400,
                color = Color.Black,
                lineHeight = 16.sp
            )
            Text(
                text = "${playlist.tracks.size} треков",
                fontSize = 11.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.W400,
                color = Color.Gray,
                lineHeight = 11.sp
            )
        }
    }
}
